package cz.muni.fi.pb162.hw03.support;

import static cz.muni.fi.pb162.hw03.support.TestSupport.subPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jludvice on 18.8.15.
 */
public class WindowsExecutor implements NativeExecutor {
    public static final Path CMD_EXE = Paths.get(System.getenv("WINDIR"), "system32", "cmd.exe");

    static int executeCommand(String... args) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(args);
        Process process = pb.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new IOException("Waiting for process to finish failed.", e);
        }
        return process.exitValue();
    }

    public void deleteRecursively(Path p) throws IOException {
        int exitValue = executeCommand(CMD_EXE.toString(), "/c", "rd", p.toAbsolutePath().toString(), "/q", "/s");
        // originaly there was check if exitValue != null, but one simply can't trust windows exit values
        if (Files.exists(p)) {
            throw new IOException("Failed to delete " + p.toString());
        }
    }

    public void copyRecursively(Path from, Path to) throws IOException {
        // linux cp -r create folder in destination,
        // but windows robocopy takes content of src folder and copies it into target
        int exitValue = executeCommand(
                CMD_EXE.toString(),
                "/c", "robocopy",
                from.toAbsolutePath().toString(),
                subPath(to.toAbsolutePath(), from.getFileName().toString()).toString(),
                "/E",
                prepareRobocopyLog());

        if (exitValue >= 4) {
            // robocopy exit codes: http://ss64.com/nt/robocopy-exit.html
            throw new IOException(String.format("Failed to copy (exit code %s) files from %s to %s", exitValue, from, to));
        }
    }

    private static String prepareRobocopyLog() throws IOException {
        Path log = Paths.get("robocopy_log.txt");
        if (!Files.exists(log)) {
            Files.createFile(log);
        }
        return "/log:\"" + log.toAbsolutePath().toString() + "\"";
    }

}
