package com.github.leomillon.properties.scanner.utils;

import com.github.leomillon.properties.scanner.EvaluatedProperty;
import com.github.leomillon.properties.scanner.Register;
import com.github.leomillon.properties.scanner.SimpleProperty;
import com.github.leomillon.properties.scanner.factory.PropertiesFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
        Register<SimpleProperty> register = PropertiesFactory.expressionsRegister();

        // When
        Register<EvaluatedProperty> result = ValueParser.evaluateProperties(register);

        // Then
        assertThat(result)
                .extracting("key", "rawValue", "interpretedValue")
                .containsOnlyOnce(
                        tuple("default.pwd", "my-password", "my-password"),
                        tuple("default.domain", "http://domain.com", "http://domain.com"),
                        tuple("service.pwd", "${default.pwd}", "my-password"),
                        tuple("service.path", "my-path", "my-path"),
                        tuple("service.url", "${default.domain}/${service.path}", "http://domain.com/my-path")
                );
    }
}