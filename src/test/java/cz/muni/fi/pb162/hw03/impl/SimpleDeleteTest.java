package cz.muni.fi.pb162.hw03.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import cz.muni.fi.pb162.hw03.FileManager;
import cz.muni.fi.pb162.hw03.support.TestSupport;

/**
 * Created by jludvice on 23.8.15.
 */
public class SimpleDeleteTest {
    public static final String SHOULD_STAY = "should_stay.doc";
    public static final String SHOULD_BE_DELETED = "should_be_deleted.odt";
    @Rule
    public TestSupport testSupport = TestSupport.forJob("del_cmd");
    private FileManager fileManager;
    private Path logPath;

    @Before
    public void before() throws Exception {
        fileManager = new FileManagerImpl();
        logPath = Paths.get("logs", "del_cmd.log").toAbsolutePath();
        fileManager.executeJob(TestSupport.JOB_FILE_PATH.toString(), logPath.toString());
    }

    @Test
    public void testDelete() {
        assertFalse("File " + SHOULD_BE_DELETED + " should exist after test.", Files.exists(TestSupport.subPath(testSupport.getJobSrc(), SHOULD_BE_DELETED)));
    }

    @Test
    public void testOtherFilesStay() {
        assertTrue("File " + SHOULD_STAY + " should exist after test.", Files.exists(TestSupport.subPath(testSupport.getJobSrc(), SHOULD_STAY)));
    }

    @Test
    public void testRecursiveDelete() {
        assertFalse("File subfolder/ " + SHOULD_BE_DELETED + " should exist after test.", Files.exists(TestSupport.subPath(testSupport.getJobSrc(), "subfolder", SHOULD_BE_DELETED)));
    }

    @Test
    public void testRecursiveOtherShouldStay() {
        assertTrue("File subfolder/" + SHOULD_STAY + " should exist after test.", Files.exists(TestSupport.subPath(testSupport.getJobSrc(), "subfolder", SHOULD_STAY)));
    }

    @Test
    public void testFoldersStay() {
        assertTrue("Src folder should stay", Files.exists(testSupport.getJobSrc()));
        Path subfolder = TestSupport.subPath(testSupport.getJobSrc(), "subfolder");
        assertTrue("Src subfolder " + subfolder.toString() + " should stay", Files.exists(subfolder));
    }

    @Test
    public void testLogFile() throws IOException {
        List<String> logLines = Files.readAllLines(logPath, FileManager.JOB_FILE_CHARSET);
        assertEquals("LogFile should contain 2 entries.", 2, logLines.size());

        for (String line : logLines) {
            String[] item = line.split(";");
            assertEquals("Log line must be in format <op>:<absolute path to file>", 2, item.length);
            assertEquals("It must be delete op", "DEL", item[0]);
            Path p = Paths.get(item[1]);
            assertTrue("It must contain absolute path to file", p.isAbsolute());
            assertFalse("That file shouldn't exist anymore", Files.exists(p));
        }
    }
}
