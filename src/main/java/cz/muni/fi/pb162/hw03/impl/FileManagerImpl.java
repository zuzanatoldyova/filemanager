package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.FileManager;
import cz.muni.fi.pb162.hw03.Operation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author Zuzana Toldyova <323119@mail.muni.cz>
 */
public class FileManagerImpl implements FileManager {

    @Override
    public void executeJob(String jobPath, String logFilePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(jobPath)))) {

            String j = br.readLine();
            String[] folders = j.split(";", 2);
            File folder = new File(folders[1]);
            String i = br.readLine();
            Operation cleanup = null;
            while (i != null) {
                String operation;
                if (i.charAt(0) == '#') {
                    i = br.readLine();
                } else if (i.equals("")) {
                    i = br.readLine();
                }
                String[] split = i.split(";");
                switch (split[0]) {
                    case "CP":
                        cleanup = new CopyOperation(Paths.get(split[2]), split[1]);
                        break;
                    case "MV":
                        cleanup = new MoveOperation(Paths.get(split[2]), split[1]);
                        break;
                    case "DEL":
                        cleanup = new MoveOperation(Paths.get(split[2]), split[1]);
                        break;
                }
                executeRecursively(folder, cleanup, logFilePath);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void executeRecursively(File file, Operation operation, String logFilePath) throws IOException {
        for (File f : file.listFiles()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(logFilePath)))) {
                if (f.isDirectory()) {
                    executeRecursively(f, operation, logFilePath);
                }
                bw.write(operation.toString() + ";");
                bw.write(f.getAbsolutePath() + ";");
                operation.execute(file.toPath());
                bw.write(f.getAbsolutePath());
            }
        }
    }

}
