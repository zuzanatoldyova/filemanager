package cz.muni.fi.pb162.hw03.support;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by jludvice on 16.8.15.
 */
public class UnixExecutor implements NativeExecutor {

    static void executeCommand(String... args) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(args);
        Process process = pb.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Waiting for process to finish failed.", e);
        }
        if (process.exitValue() != 0) {
            throw new IOException("Failed to execute command: " + pb.command());
        }
    }

    public void deleteRecursively(Path p) throws IOException {
        executeCommand("rm", "-rf", p.toAbsolutePath().toString());
    }

    public void copyRecursively(Path from, Path to) throws IOException {
        executeCommand("cp", "-r", from.toAbsolutePath().toString(), to.toAbsolutePath().toString());
    }
}
