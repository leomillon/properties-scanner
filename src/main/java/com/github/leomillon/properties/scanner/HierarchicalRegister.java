package com.github.leomillon.properties.scanner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.*;

public class HierarchicalRegister<T extends Property> {

    @Nonnull
    private final Map<PropFile, Register<T>> registers;
    @Nonnull
    private final List<PropFile> filesOrder;

    private HierarchicalRegister(Builder<T> builder) {
        this.registers = ImmutableMap.copyOf(builder.registers);
        this.filesOrder = ImmutableList.copyOf(builder.filesOrder);
    }

    @Nonnull
    public static <V extends Property> Builder<V> builder() {
        return new Builder<>();
    }

    @Nonnull
    public Register<T> getFinalRegister() {
        Register.Builder<T> finalRegister = Register.builder();
        for (PropFile propFile : filesOrder) {
            Register<T> currentRegister = registers.get(propFile);
            currentRegister.getProperties().forEach(finalRegister::addProperty);
        }
        return finalRegister.build();
    }

    @Nonnull
    public Register<HistorizedProperty> getHistorizedProperties() {
        Register.Builder<HistorizedProperty> historizedPropertiesRegister = Register.builder();
        for (T property : getFinalRegister()) {
            String propertyKey = property.getKey();
            historizedPropertiesRegister.addProperty(createHistorizedPropertyForKey(propertyKey));
        }
        return historizedPropertiesRegister.build();
    }

    @Nonnull
    private HistorizedProperty createHistorizedPropertyForKey(String propertyKey) {
        Map<PropFile, String> propertyValues = new HashMap<>();
        for (Map.Entry<PropFile, Register<T>> registerEntry : registers.entrySet()) {
            PropFile propFile = registerEntry.getKey();
            Optional<T> propertyForKey = registerEntry.getValue().getPropertyForKey(propertyKey);
            if (propertyForKey.isPresent()) {
                propertyValues.put(propFile, propertyForKey.get().getValue());
            }
        }
        return new HistorizedProperty(propertyKey, propertyValues, filesOrder);
    }

    public static class Builder<T extends Property> {

        @Nonnull
        private final Map<PropFile, Register<T>> registers = new HashMap<>();
        @Nonnull
        private final List<PropFile> filesOrder = new ArrayList<>();

        @Nonnull
        public Builder<T> addNextRegisterForFile(@Nonnull Register<T> register, @Nonnull PropFile file) {
            addRegisterForFile(register, file);
            addFileToFilesOrder(file);
            return this;
        }

        private void addRegisterForFile(@Nonnull Register<T> register, @Nonnull PropFile file) {
            registers.put(requireNonNull(file), requireNonNull(register));
        }

        private void addFileToFilesOrder(@Nonnull PropFile file) {
            filesOrder.add(requireNonNull(file));
        }

        @Nonnull
        public HierarchicalRegister<T> build() {
            return new HierarchicalRegister<>(this);
        }
    }
}
