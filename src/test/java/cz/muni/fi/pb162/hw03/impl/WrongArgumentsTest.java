package cz.muni.fi.pb162.hw03.impl;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

import cz.muni.fi.pb162.hw03.FileManager;
import cz.muni.fi.pb162.hw03.support.TestSupport;

/**
 * Created by jludvice on 16.8.15.
 */
public class WrongArgumentsTest {
    public static final String JOB_NAME = "job1";
    @Rule
    public TestSupport support = TestSupport.forJob(JOB_NAME);
    FileManager fm;

    @Before
    public void init() {
        fm = new FileManagerImpl();
    }

    @Test
    public void testNullJobFile() {
        try {
            fm.executeJob(null, "logs/logfile");
            fail("It should fail when null argument given.");
        } catch (Exception e) {
            // it should pass
        }
    }

    @Test
    public void testNullLogFile() {
        try {
            fm.executeJob(TestSupport.JOB_FILE_PATH.toString(), null);
            fail("It should fail when null argument given.");
        } catch (Exception e) {
            // it should fail
        }
    }

    @Test
    public void testNonExistentJobFile() {
        try {
            fm.executeJob("some/nonexistent/path", "logs/logfile");
            fail("FileManager can't work with non-existent job file");
        } catch (Exception e) {
            // It's ok to throw any exception you consider appropriate, checked or unchecked.
            // Also java already has exception for situations where file couldn't be found.
        }
    }

    @Test
    public void testInvalidLogPath() {
        try {
            fm.executeJob(TestSupport.JOB_FILE_PATH.toString(), TestSupport.WORK_DEST + File.separator + "folder&&;:\0<>|\\");
            fail("It shouldn't be possible to create file with illegal characters in name.");
        } catch (Exception e) {
            // it should fail
        }
    }
}
