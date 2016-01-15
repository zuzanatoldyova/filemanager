package cz.muni.fi.pb162.hw03.impl;

import java.nio.file.Path;

/**
 *
 * @author Zuzana Toldyova <323119@mail.muni.cz>
 */
public class OperationUtils {

    /**
     * Adds counter to existing file path.
     *
     * @param path path to file
     * @return new path with a counter
     */
    public static Path addCounter(Path path) {
        int i = 1;
        while (path.toFile().exists()) {
            String fileName = path.toFile().getName();
            int pos = fileName.lastIndexOf(".");
            String extension = String.format("_%03d", i);
            StringBuilder sb = new StringBuilder(fileName).insert(pos, extension);
            path = path.resolveSibling(sb.toString());
            i++;
        }
        return path;
    }
}
