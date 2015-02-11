package com.github.leomillon.properties.scanner.utils;

import com.github.leomillon.properties.scanner.HierarchicalRegister;
import com.github.leomillon.properties.scanner.SimpleProperty;
import com.google.common.io.Resources;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class LoaderTest {

    private static final String FIRST_PROP_FILE_NAME = "first.properties";
    private static final String SECOND_PROP_FILE_NAME = "second.properties";

    @Test
    public void should_load_properties_from_file_paths() throws IOException {

        // Given
        String firstFilePath = Resources.getResource(FIRST_PROP_FILE_NAME).getPath();
        String secondFilePath = Resources.getResource(SECOND_PROP_FILE_NAME).getPath();

        // When
        HierarchicalRegister<SimpleProperty> result = Loader.loadProperties(firstFilePath, secondFilePath);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFinalRegister())
                .extracting("key", "value")
                .containsOnlyOnce(
                        tuple("sample.property.key", "false"),
                        tuple("an.other.property", "some text value"),
                        tuple("a.multiline.value", "A value on multiple lines."),
                        tuple("an.new.property", "Oh! A property...")
                );
    }

}