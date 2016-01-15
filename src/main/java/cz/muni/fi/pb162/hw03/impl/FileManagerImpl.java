package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.FileManager;
import cz.muni.fi.pb162.hw03.Operation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Zuzana Toldyova <323119@mail.muni.cz>
 */
public class FileManagerImpl implements FileManager {

    @Override
    public void executeJob(String jobPath, String logFilePath) throws Exception {
        Path jobFile = Paths.get(jobPath);
        if (!Files.exists(jobFile)) {
            throw new FileNotFoundException();
        }
        Path logPath = Paths.get(logFilePath);
        Files.createDirectories(logPath.getParent());
        try (BufferedReader br = new BufferedReader(new FileReader(new File(jobPath)));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFilePath), "utf-8"))) {

            String j = br.readLine();
            String[] folders = j.split(";", 2);
            File folder = new File(folders[1]);
            String i = br.readLine();
            Operation cleanup;
            while (i != null) {
                if (i.startsWith("#")) {
                    i = br.readLine();
                } else if (i.equals("")) {
                    i = br.readLine();
                } else {
                    String[] split = i.split(";");
                    switch (split[0]) {
                        case "CP":
                            cleanup = new CopyOperation(Paths.get(split[2]), split[1]);
                            break;
                        case "MV":
                            cleanup = new MoveOperation(Paths.get(split[2]), split[1]);
                            break;
                        case "DEL":
                            cleanup = new DeleteOperation(split[1]);
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                    executeRecursively(folder, cleanup, bw);
                    i = br.readLine();
                }
            }
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

    }

    /**
     * Executes job recursively on the files.
     *
     * @param file folder where to look for files
     * @param operation which operation should be executed
     * @param bw writes a log
     * @throws IOException on IO error
     */
    public static void executeRecursively(File file, Operation operation, BufferedWriter bw) throws IOException {
        for (File f : file.listFiles()) {
            if (operation.execute(f.toPath())) {
                bw.write(operation.toString());
                bw.newLine();
                bw.flush();
            }
            if (f.isDirectory()) {
                executeRecursively(f, operation, bw);
            }
        }
    }
}
