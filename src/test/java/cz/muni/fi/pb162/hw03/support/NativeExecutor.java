package cz.muni.fi.pb162.hw03.support;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by jludvice on 16.8.15.
 */
public interface NativeExecutor {
    void deleteRecursively(Path p) throws IOException;

    void copyRecursively(Path from, Path to) throws IOException;
}
