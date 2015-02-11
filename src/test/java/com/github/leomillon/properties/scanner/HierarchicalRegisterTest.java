package com.github.leomillon.properties.scanner;

import com.github.leomillon.properties.scanner.factory.PropertiesFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class HierarchicalRegisterTest {

    private HierarchicalRegister<SimpleProperty> register;

    @BeforeMethod
    public void setUp() throws Exception {
        register = PropertiesFactory.defaultRegister();
    }

    @Test
    public void should_test_unique_property_value_history() {

        // When
        Register<HistorizedProperty> result = register.getHistorizedProperties();

        // Then
        assertThat(result).isNotNull();

        Optional<HistorizedProperty> uniqueValueProp = result.getPropertyForKey("an.other.property");
        assertThat(uniqueValueProp.isPresent()).isTrue();
        assertThat(uniqueValueProp.get().getState()).isEqualTo(HistorizedProperty.State.UNIQUE);
        assertThat(uniqueValueProp.get().getHistory().keySet())
                .extracting("name")
                .containsExactly(PropertiesFactory.FIRST_PROP_FILE_NAME);
        assertThat(uniqueValueProp.get().getHistory().values())
                .containsExactly("some text value");
        assertThat(uniqueValueProp.get().getLastValue())
                .isEqualTo("some text value");
    }

    @Test
    public void should_test_overriden_property_value_history() {

        // When
        Register<HistorizedProperty> result = register.getHistorizedProperties();

        // Then
        assertThat(result).isNotNull();

        Optional<HistorizedProperty> overridenValuesProp = result.getPropertyForKey("sample.property.key");
        assertThat(overridenValuesProp.isPresent()).isTrue();
        assertThat(overridenValuesProp.get().getState()).isEqualTo(HistorizedProperty.State.OVERRIDEN);
        assertThat(overridenValuesProp.get().getHistory().keySet())
                .extracting("name")
                .containsExactly(
                        PropertiesFactory.FIRST_PROP_FILE_NAME,
                        PropertiesFactory.SECOND_PROP_FILE_NAME
                );
        assertThat(overridenValuesProp.get().getHistory().values())
                .containsExactly(
                        "true",
                        "false"
                );
        assertThat(overridenValuesProp.get().getLastValue())
                .isEqualTo("false");
    }

    @Test
    public void should_test_duplicated_property_value_history() {

        // When
        Register<HistorizedProperty> result = register.getHistorizedProperties();

        // Then
        assertThat(result).isNotNull();

        Optional<HistorizedProperty> duplicatedValuesProp = result.getPropertyForKey("a.duplicated.property");
        assertThat(duplicatedValuesProp.isPresent()).isTrue();
        assertThat(duplicatedValuesProp.get().getState()).isEqualTo(HistorizedProperty.State.DUPLICATED);
        assertThat(duplicatedValuesProp.get().getHistory().keySet())
                .extracting("name")
                .containsExactly(
                        PropertiesFactory.FIRST_PROP_FILE_NAME,
                        PropertiesFactory.SECOND_PROP_FILE_NAME
                );
        assertThat(duplicatedValuesProp.get().getHistory().values())
                .containsExactly(
                        "sameValue",
                        "sameValue"
                );
        assertThat(duplicatedValuesProp.get().getLastValue())
                .isEqualTo("sameValue");
    }
}