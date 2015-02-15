package com.github.leomillon.properties.scanner.utils;

import com.github.leomillon.properties.scanner.HierarchicalRegister;
import com.github.leomillon.properties.scanner.PropFile;
import com.github.leomillon.properties.scanner.Register;
import com.github.leomillon.properties.scanner.SimpleProperty;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public final class Loader {

    private Loader() {
        // Prevent instanciation
    }

    @Nonnull
    public static HierarchicalRegister<SimpleProperty> loadPropertiesInOrder(String... filePaths) throws IOException {

        HierarchicalRegister.Builder<SimpleProperty> hierarchycalRegisterBuilder = HierarchicalRegister.builder();
        int index = 0;
        for (String filePath : filePaths) {
            File file = new File(filePath);
            PropFile propertiesFile = new PropFile(String.valueOf(index++), file.getName());
            try (FileInputStream fileIS = new FileInputStream(file)) {
                hierarchycalRegisterBuilder.addNextRegisterForFile(toRegister(fileIS), propertiesFile);
            }
        }
        return hierarchycalRegisterBuilder.build();
    }

    @Nonnull
    public static Register<SimpleProperty> toRegister(@Nonnull InputStream inputStream) throws IOException {

        Properties props = new Properties();
        props.load(inputStream);

        return toRegister(props);
    }

    @Nonnull
    public static Register<SimpleProperty> toRegister(@Nonnull Properties props) {
        return toRegister(props.entrySet());
    }

    @Nonnull
    public static Register<SimpleProperty> toRegister(@Nonnull Set<Map.Entry<Object, Object>> entries) {
        Register.Builder<SimpleProperty> register = Register.builder();
        for (Map.Entry<Object, Object> propEntry : entries) {
            register.addProperty(
                    new SimpleProperty(String.valueOf(propEntry.getKey()), String.valueOf(propEntry.getValue()))
            );
        }
        return register.build();
    }

}
