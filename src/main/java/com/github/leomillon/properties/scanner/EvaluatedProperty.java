package com.github.leomillon.properties.scanner;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Objects.*;

public class EvaluatedProperty implements Property {

    @Nonnull
    private final SimpleProperty property;
    @Nullable
    private final String interpretedValue;

    public EvaluatedProperty(@Nonnull SimpleProperty property, @Nullable String interpretedValue) {
        this.property = requireNonNull(property);
        this.interpretedValue = interpretedValue;
    }

    @Nullable
    public String getInterpretedValue() {
        return interpretedValue;
    }

    @Nonnull
    public String getKey() {
        return property.getKey();
    }

    @Nullable
    public String getRawValue() {
        return property.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvaluatedProperty that = (EvaluatedProperty) o;

        if (interpretedValue != null ? !interpretedValue.equals(that.interpretedValue) : that.interpretedValue != null)
            return false;
        if (!property.equals(that.property)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = property.hashCode();
        result = 31 * result + (interpretedValue != null ? interpretedValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("property", property)
                .add("interpretedValue", interpretedValue)
                .toString();
    }
}
