package com.github.leomillon.properties.scanner;

import com.google.common.base.MoreObjects;

import javax.annotation.Nonnull;

import static java.util.Objects.*;

public class PropFile {

    @Nonnull
    private final String id;
    @Nonnull
    private final String name;

    public PropFile(@Nonnull String id, @Nonnull String name) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PropFile propFile = (PropFile) o;

        if (!id.equals(propFile.id)) return false;
        if (!name.equals(propFile.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .toString();
    }
}
