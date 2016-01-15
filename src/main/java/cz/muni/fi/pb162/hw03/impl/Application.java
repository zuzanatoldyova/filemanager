package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.FileManager;
import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;

/**
 *
 * @author Zuzana Toldyova <323119@mail.muni.cz>
 */
public class Application {

    /**
     * Initiates the filemanager application.
     *
     * @param args path to jobfile and logfile
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("For correct usage, please insert path to jobfile and path to logfile.");
            System.exit(1);
        } else {
            File file1 = new File(args[0]);
            File file2 = new File(args[1]);
            while (file2 != null && !file2.exists()) {
                file2 = file2.getParentFile();
            }
            if (file1.exists() && file2 != null) {
                try {
                    FileManager fm = new FileManagerImpl();
                    fm.executeJob(args[0], args[1]);
                    System.out.println("Success. You can file logfile here: " + args[1] + ".");
                } catch (Exception ex) {
                    System.err.println("Error.");
                }
            } else {
                System.out.println("For correct usage, please insert path to jobfile and path to logfile.");
                System.exit(1);
            }
        }
    }
}
