package com.github.leomillon.properties.scanner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Property {

    @Nonnull
    String getKey();

    @Nullable
    String getValue();
}
