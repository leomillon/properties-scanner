package com.github.leomillon.properties.scanner;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class PropertyOrdering {

    @Nonnull
    public static Ordering<Property> keyInNaturalOrder() {
        return Ordering.natural().nullsLast().onResultOf(PropertyToKey.instance());
    }

    private enum PropertyToKey implements Function<Property, String> {
        INSTANCE;

        @Nonnull
        public static PropertyToKey instance() {
            return INSTANCE;
        }

        @Nullable
        @Override
        public String apply(Property input) {
            return input == null ? null : input.getKey();
        }
    }
}
