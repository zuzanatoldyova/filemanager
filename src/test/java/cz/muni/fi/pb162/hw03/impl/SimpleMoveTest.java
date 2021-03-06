package cz.muni.fi.pb162.hw03.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static cz.muni.fi.pb162.hw03.support.TestSupport.subPath;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import cz.muni.fi.pb162.hw03.FileManager;
import cz.muni.fi.pb162.hw03.support.TestSupport;

/**
 * Created by jludvice on 24.8.15.
 */
public class SimpleMoveTest {
    public static final String FILE_NAME = "document.odt";
    @Rule
    public TestSupport testSupport = TestSupport.forJob("mv_cmd");
    private FileManager fileManager;
    private Path logPath;

    @Before
    public void before() throws Exception {
        fileManager = new FileManagerImpl();
        logPath = Paths.get("logs", "mv_cmd.log").toAbsolutePath();

        fileManager.executeJob(TestSupport.JOB_FILE_PATH.toString(), logPath.toString());
    }

    @Test
    public void testFileMoved() {
        assertFalse("File " + FILE_NAME + "shouldn't be in src folder", Files.exists(subPath(testSupport.getJobSrc(), FILE_NAME)));

        assertTrue("File " + FILE_NAME + " should be in destination directory", Files.exists(subPath(TestSupport.WORK_DEST, "docs", FILE_NAME)));
    }
}
