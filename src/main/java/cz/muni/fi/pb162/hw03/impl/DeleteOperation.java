package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.Operation;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;

/**
 *
 * @author Zuzana Toldyova <323119@mail.muni.cz>
 */
public class DeleteOperation implements Operation {

    private final PathMatcher matcher;
    private Path fromPath;

    /**
     * Creates new DeleteOperation.
     *
     * @param pattern which kind of files should be deleted
     */
    public DeleteOperation(String pattern) {
        this.matcher = FileSystems.getDefault().getPathMatcher("glob:**." + pattern);
    }

    @Override
    public boolean execute(Path path) throws IOException {
        if (matcher.matches(path)) {
            Files.delete(path);
            setFromPath(path);
            return true;
        }

        return false;
    }

    public void setFromPath(Path fromPath) {
        this.fromPath = fromPath;
    }

    @Override
    public String toString() {
        return "DEL;" + fromPath;
    }
}
