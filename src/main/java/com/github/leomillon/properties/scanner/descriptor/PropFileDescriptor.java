package com.github.leomillon.properties.scanner.descriptor;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;

public interface PropFileDescriptor {

    /**
     * Generate an id for the target {@link com.github.leomillon.properties.scanner.PropFile}.
     * @param index the index of the generated {@link com.github.leomillon.properties.scanner.PropFile}.
     */
    @Nonnull
    String getId(int index);

    /**
     * Generate a name for the target {@link com.github.leomillon.properties.scanner.PropFile}.
     * @param index the index of the generated {@link com.github.leomillon.properties.scanner.PropFile}.
     */
    @Nonnull
    String getName(int index);

    /**
     * Get the input stream to read the property file.
     */
    @Nonnull
    InputStream getInputStream() throws IOException;

}
