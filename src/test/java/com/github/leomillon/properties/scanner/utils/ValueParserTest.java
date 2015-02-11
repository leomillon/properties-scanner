package com.github.leomillon.properties.scanner.utils;

import com.github.leomillon.properties.scanner.EvaluatedProperty;
import com.github.leomillon.properties.scanner.Register;
import com.github.leomillon.properties.scanner.SimpleProperty;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Nonnull;

import static org.assertj.core.api.Assertions.*;

public class ValueParserTest {

    @Test(dataProvider = "content_to_extract_provider")
    public void should_extract_expression_keys(String content, String[] expectedKeys) {
        assertThat(ValueParser.extractKeysFromExpressions(content)).containsExactly(expectedKeys);
    }

    @DataProvider
    private Object[][] content_to_extract_provider() {
        return new Object[][]{
                {null, new String[]{}},
                {"content.without.expression", new String[]{}},
                {"${expression}", new String[]{"expression"}},
                {"${expression.with.dots}", new String[]{"expression.with.dots"}},
                {"start${expression}end", new String[]{"expression"}},
                {"start${expression.1}middle${expression.2}end", new String[]{"expression.1", "expression.2"}},
        };
    }

    @Test
    public void should_find_values_for_expressions() throws Exception {

        // Given
        Register.Builder<SimpleProperty> register = Register.builder();

        String defaultPwdKey = "default.pwd";
        String defaultPwdValue = "my-password";
        register.addProperty(prop(defaultPwdKey, defaultPwdValue));

        String defaultDomainKey = "default.domain";
        String defaultDomainValue = "http://domain.com";
        register.addProperty(prop(defaultDomainKey, defaultDomainValue));

        String servicePwdKey = "service.pwd";
        String servicePwdValue = "${" + defaultPwdKey + "}";
        register.addProperty(prop(servicePwdKey, servicePwdValue));

        String servicePathKey = "service.path";
        String servicePathValue = "my-path";
        register.addProperty(prop(servicePathKey, servicePathValue));

        String serviceUrlKey = "service.url";
        String serviceUrlValue = "${" + defaultDomainKey + "}/${" + servicePathKey + "}";
        register.addProperty(prop(serviceUrlKey, serviceUrlValue));

        // When
        Register<EvaluatedProperty> result = ValueParser.evaluateProperties(register.build());

        // Then
        assertThat(result)
                .extracting("key", "rawValue", "interpretedValue")
                .containsOnlyOnce(
                        tuple(defaultPwdKey, defaultPwdValue, defaultPwdValue),
                        tuple(defaultDomainKey, defaultDomainValue, defaultDomainValue),
                        tuple(servicePwdKey, servicePwdValue, defaultPwdValue),
                        tuple(servicePathKey, servicePathValue, servicePathValue),
                        tuple(serviceUrlKey, serviceUrlValue, defaultDomainValue + "/" + servicePathValue)
                );
    }

    private static SimpleProperty prop(@Nonnull String key, String value) {
        return new SimpleProperty(key, value);
    }
}