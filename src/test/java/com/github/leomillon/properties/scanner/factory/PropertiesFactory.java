package com.github.leomillon.properties.scanner.factory;

import com.github.leomillon.properties.scanner.HierarchicalRegister;
import com.github.leomillon.properties.scanner.Register;
import com.github.leomillon.properties.scanner.SimpleProperty;
import com.github.leomillon.properties.scanner.utils.Loader;
import com.google.common.io.Resources;

import java.io.IOException;

public final class PropertiesFactory {

    public static final String FIRST_PROP_FILE_NAME = "first.properties";
    public static final String SECOND_PROP_FILE_NAME = "second.properties";
    public static final String EXPRESSIONS_PROP_FILE_NAME = "expressions.properties";

    public static HierarchicalRegister<SimpleProperty> defaultRegister() throws IOException {
        return createRegisterFromFiles(filePath(FIRST_PROP_FILE_NAME), filePath(SECOND_PROP_FILE_NAME));
    }

    public static Register<SimpleProperty> expressionsRegister() throws IOException {
        return createRegisterFromFiles(filePath(EXPRESSIONS_PROP_FILE_NAME)).getFinalRegister();
    }

    public static String filePath(String fileName) {
        return Resources.getResource(fileName).getPath();
    }

    public static HierarchicalRegister<SimpleProperty> createRegisterFromFiles(String... filePathes) throws IOException {
        return Loader.loadPropertiesInOrder(filePathes);
    }

}
