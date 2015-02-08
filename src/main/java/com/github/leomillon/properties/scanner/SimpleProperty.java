package com.github.leomillon.properties.scanner;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.*;

public class SimpleProperty implements Property {

    @Nonnull
    private final String key;
    @Nullable
    private final String value;

    public SimpleProperty(@Nonnull String key, @Nullable Object value) {
        this.key = requireNonNull(key);
        this.value = value == null ? null : String.valueOf(value);
    }

    @Override
    @Nonnull
    public String getKey() {
        return key;
    }

    @Nullable
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleProperty property = (SimpleProperty) o;

        if (!key.equals(property.key)) return false;
        if (value != null ? !value.equals(property.value) : property.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("key", key)
                .add("value", value)
                .toString();
    }
}
