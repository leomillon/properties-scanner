package com.github.leomillon.properties.scanner.utils;

import com.github.leomillon.properties.scanner.HierarchicalRegister;
import com.github.leomillon.properties.scanner.Register;
import com.github.leomillon.properties.scanner.SimpleProperty;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

public class LoaderTest {

    private static final String FIRST_PROP_FILE_NAME = "first.properties";
    private static final String SECOND_PROP_FILE_NAME = "second.properties";
    private static final String PROP_KEY_FIELD_NAME = "key";
    private static final String PROP_VALUE_FIELD_NAME = "value";

    private String firstFilePath;

    @BeforeMethod
    public void setUp() throws Exception {
        firstFilePath = getFilePath(FIRST_PROP_FILE_NAME);
    }

    private static String getFilePath(String fileName) {
        return Resources.getResource(fileName).getPath();
    }

    @Test
    public void should_load_properties_from_file_paths() throws IOException {

        // Given
        String secondFilePath = getFilePath(SECOND_PROP_FILE_NAME);

        // When
        HierarchicalRegister<SimpleProperty> result = Loader.loadPropertiesInOrder(firstFilePath, secondFilePath);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFinalRegister())
                .extracting(PROP_KEY_FIELD_NAME, PROP_VALUE_FIELD_NAME)
                .containsOnlyOnce(
                        tuple("sample.property.key", "false"),
                        tuple("an.other.property", "some text value"),
                        tuple("a.multiline.value", "A value on multiple lines."),
                        tuple("an.new.property", "Oh! A property...")
                );
    }

    @Test
    public void should_convert_input_stream_to_register() throws Exception {

        // Given
        FileInputStream inputStream = new FileInputStream(firstFilePath);

        // When
        Register<SimpleProperty> result = Loader.toRegister(inputStream);

        // Then
        assertThat(result)
                .extracting(PROP_KEY_FIELD_NAME, PROP_VALUE_FIELD_NAME)
                .containsOnlyOnce(
                        tuple("sample.property.key", "true"),
                        tuple("an.other.property", "some text value"),
                        tuple("a.duplicated.property", "sameValue"),
                        tuple("a.multiline.value", "A value on multiple lines.")
                );
    }

    @Test
    public void should_convert_properties_to_register() throws Exception {

        // Given
        FileInputStream inputStream = new FileInputStream(firstFilePath);
        Properties properties = new Properties();
        properties.load(inputStream);

        // When
        Register<SimpleProperty> result = Loader.toRegister(properties);

        // Then
        assertThat(result)
                .extracting(PROP_KEY_FIELD_NAME, PROP_VALUE_FIELD_NAME)
                .containsOnlyOnce(
                        tuple("sample.property.key", "true"),
                        tuple("an.other.property", "some text value"),
                        tuple("a.duplicated.property", "sameValue"),
                        tuple("a.multiline.value", "A value on multiple lines.")
                );
    }

    @Test
    public void should_convert_map_to_register() {

        // Given
        String propKey1 = "property.one";
        String propValue1 = "valueOne";
        String propKey2 = "property.two";
        String propValue2 = "valueTwo";
        Map<Object, Object> entries = ImmutableMap.builder()
                .put(propKey1, propValue1)
                .put(propKey2, propValue2)
                .build();

        // When
        Register<SimpleProperty> result = Loader.toRegister(entries.entrySet());

        // Then
        assertThat(result)
                .extracting(PROP_KEY_FIELD_NAME, PROP_VALUE_FIELD_NAME)
                .containsOnlyOnce(
                        tuple(propKey1, propValue1),
                        tuple(propKey2, propValue2)
                );
    }
}