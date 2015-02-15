package com.github.leomillon.properties.scanner;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.*;

public class Register<T extends Property> implements Iterable<T> {

    @Nonnull
    private final Map<String, T> properties;

    private Register(Builder<T> builder) {
        this.properties = ImmutableMap.copyOf(builder.properties);
    }

    @Nonnull
    public static <V extends Property> Builder<V> builder() {
        return new Builder<>();
    }

    @Nonnull
    public Optional<T> getPropertyForKey(@Nonnull String key) {
        return Optional.ofNullable(properties.get(key));
    }

    @Nonnull
    public Set<String> getKeys() {
        return properties.keySet();
    }

    @Nonnull
    public Set<T> getProperties() {
        return ImmutableSet.copyOf(getSortedProperties());
    }

    @Override
    public Iterator<T> iterator() {
        return getSortedProperties().iterator();
    }

    @Nonnull
    private List<T> getSortedProperties() {
        return PropertyOrdering.keyInNaturalOrder().immutableSortedCopy(properties.values());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("properties", properties)
                .toString();
    }

    public static class Builder<T extends Property> {

        @Nonnull
        private Map<String, T> properties = new HashMap<>();

        @Nonnull
        public Builder<T> addProperty(@Nonnull T property) {
            properties.put(requireNonNull(property).getKey(), property);
            return this;
        }

        @Nonnull
        public Register<T> build() {
            return new Register<>(this);
        }
    }
}
