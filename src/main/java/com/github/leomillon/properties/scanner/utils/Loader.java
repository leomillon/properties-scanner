package com.github.leomillon.properties.scanner.utils;

import com.github.leomillon.properties.scanner.HierarchicalRegister;
import com.github.leomillon.properties.scanner.PropFile;
import com.github.leomillon.properties.scanner.Register;
import com.github.leomillon.properties.scanner.SimpleProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public final class Loader {

    private Loader() {
        // Prevent instanciation
    }

    public static HierarchicalRegister<SimpleProperty> loadProperties(String... filePaths) throws IOException {

        HierarchicalRegister.Builder<SimpleProperty> hierarchycalRegisterBuilder = HierarchicalRegister.builder();
        int index = 0;
        for (String filePath : filePaths) {
            File file = new File(filePath);
            PropFile propertiesFile = new PropFile(String.valueOf(index++), file.getName());
            try (FileInputStream fileIS = new FileInputStream(file)) {
                hierarchycalRegisterBuilder.addNextRegisterForFile(loadPropertiesFromInput(fileIS), propertiesFile);
            }
        }
        return hierarchycalRegisterBuilder.build();
    }

    private static Register<SimpleProperty> loadPropertiesFromInput(FileInputStream fileIS) throws IOException {

            Properties props = new Properties();
            props.load(fileIS);

            Register.Builder<SimpleProperty> register = Register.builder();
            for (Map.Entry<Object, Object> propEntry : props.entrySet()) {
                register.addProperty(
                        new SimpleProperty(String.valueOf(propEntry.getKey()), String.valueOf(propEntry.getValue()))
                );
            }
            return register.build();
    }

}
