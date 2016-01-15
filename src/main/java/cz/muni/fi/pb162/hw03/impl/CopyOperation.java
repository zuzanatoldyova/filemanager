package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.Operation;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author Zuzana Toldyova <323119@mail.muni.cz>
 */
public class CopyOperation implements Operation {

    private final Path toPath;
    private final PathMatcher matcher;
    private Path fromPath;
    private Path targetPath;

    /**
     * Creates new CopyOperation and directories for this operation.
     *
     * @param toPath path where to copy files
     * @param pattern indicates what kind of files to copy
     * @throws java.io.IOException on error
     */
    public CopyOperation(Path toPath, String pattern) throws IOException {
        this.toPath = toPath;
        this.matcher = FileSystems.getDefault().getPathMatcher("glob:**." + pattern);
        Files.createDirectories(toPath);
    }

    @Override
    public boolean execute(Path path) throws IOException {
        if (matcher.matches(path)) {
            Path newPath = toPath.resolve(path.getFileName());
            newPath = OperationUtils.addCounter(newPath);
            Files.copy(path, newPath, StandardCopyOption.REPLACE_EXISTING);
            setFromPath(path);
            setTargetPath(newPath);
            return true;
        }
        return false;
    }

    public void setFromPath(Path fromPath) {
        this.fromPath = fromPath;
    }

    public void setTargetPath(Path newPath) {
        this.targetPath = newPath;
    }

    @Override
    public String toString() {
        return "CP;" + fromPath + ";" + targetPath;
    }

}
