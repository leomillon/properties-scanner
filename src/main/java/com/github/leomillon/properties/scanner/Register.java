package com.github.leomillon.properties.scanner;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.*;

public class Register<T extends Property> implements Iterable<T> {

    @Nonnull
    private final Map<String, T> properties;

    private Register(Builder<T> builder) {
        this.properties = ImmutableMap.copyOf(builder.properties);
    }

    @Nullable
    public T getPropertyForKey(@Nonnull String key) {
        return properties.get(key);
    }

    @Nonnull
    public Set<String> getKeys() {
        return properties.keySet();
    }

    @Nonnull
    public Set<T> getProperties() {
        return ImmutableSet.copyOf(properties.values());
    }

    @Override
    public Iterator<T> iterator() {
        return properties.values().iterator();
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
