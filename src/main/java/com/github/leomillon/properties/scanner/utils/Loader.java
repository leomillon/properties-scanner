package com.github.leomillon.properties.scanner.utils;

import com.github.leomillon.properties.scanner.HierarchicalRegister;
import com.github.leomillon.properties.scanner.PropFile;
import com.github.leomillon.properties.scanner.descriptor.PropFileDescriptor;
import com.github.leomillon.properties.scanner.Register;
import com.github.leomillon.properties.scanner.SimpleProperty;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static java.util.Objects.*;

public final class Loader {

    private Loader() {
        // Prevent instanciation
    }

    @Nonnull
    public static HierarchicalRegister<SimpleProperty> loadPropertiesInOrder(Iterable<PropFileDescriptor> propFileDescriptors)
            throws IOException {

        HierarchicalRegister.Builder<SimpleProperty> hierarchycalRegisterBuilder = HierarchicalRegister.builder();
        int index = 0;
        for (PropFileDescriptor propFileDescriptor : propFileDescriptors) {
            PropFile propertiesFile = new PropFile(propFileDescriptor.getId(index), propFileDescriptor.getName(index));
            try (InputStream propInputStream = propFileDescriptor.getInputStream()) {
                hierarchycalRegisterBuilder
                        .addNextRegisterForFile(toRegister(propInputStream), propertiesFile);
            }
            index++;
        }
        return hierarchycalRegisterBuilder.build();
    }

    @Nonnull
    public static Register<SimpleProperty> toRegister(@Nonnull InputStream inputStream) throws IOException {

        requireNonNull(inputStream, "Property file input stream is required");

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
