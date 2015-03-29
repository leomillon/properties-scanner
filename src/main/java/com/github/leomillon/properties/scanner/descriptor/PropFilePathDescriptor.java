package com.github.leomillon.properties.scanner.descriptor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.*;

public class PropFilePathDescriptor implements PropFileDescriptor {

    @Nonnull
    private final String filePath;
    @Nullable
    private File file;

    public PropFilePathDescriptor(@Nonnull String filePath) {
        this.filePath = requireNonNull(filePath);
    }

    @Nonnull
    @Override
    public String getId(int index) {
        return String.valueOf(index);
    }

    @Nonnull
    @Override
    public String getName(int index) {
        return retrieveOrGetFile().getName();
    }

    @Nonnull
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(retrieveOrGetFile());
    }

    @Nonnull
    private File retrieveOrGetFile() {
        if (file == null) {
            file = retrieveFile();
        }
        return file;
    }

    @Nonnull
    private File retrieveFile() {
        return new File(filePath);
    }
}
