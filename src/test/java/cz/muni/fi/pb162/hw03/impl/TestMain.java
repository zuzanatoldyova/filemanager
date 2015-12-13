package cz.muni.fi.pb162.hw03.impl;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import cz.muni.fi.pb162.hw03.support.TestSupport;

/**
 * Created by jludvice on 19.11.15.
 */
public class TestMain {

    public static final String LINE_SEP = System.getProperty("line.separator");

    @Rule
    public final SystemErrRule sysErr = new SystemErrRule()
            .enableLog();

    @Rule
    public final SystemOutRule sysOut = new SystemOutRule()
            .enableLog();

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void exitOneWithoutParams() {

        exit.expectSystemExitWithStatus(1);
        exit.checkAssertionAfterwards(() -> {
            assertThat("Error output should be empty", sysErr.getLog(), isEmptyOrNullString());
            assertThat("Standard output should contain usage info", sysOut.getLog().toLowerCase(), allOf(
                            notNullValue(),
                            containsString("usage"))
            );
        });

        Main.main(new String[0]);
    }

    @Test
    public void testJob1() throws IOException {
        // prepare test data for job1
        TestSupport testSupport = TestSupport.forJob("job1");
        testSupport.init();

        Path logPath = Paths.get("logs", "job1.log").toAbsolutePath();

        exit.checkAssertionAfterwards(() -> {
            assertThat("Std out should contain path to created log.", sysOut.getLog(), containsString(logPath.getFileName().toString()));
            assertThat("Std out should not contain any stacktrace.",
                    sysOut.getLog().split(LINE_SEP),
                    arrayWithSize(lessThan(4)));

            assertThat("Error output should be empty.", sysErr.getLog(), isEmptyOrNullString());
        });

        String jobFilePath = testSupport.JOB_FILE_PATH.toString();

        Main.main(new String[] {jobFilePath, logPath.toString()});
    }

    @Test
    public void testIncorrectParams() {
        // main should exit with 1
        exit.expectSystemExitWithStatus(1);
        // check std out for too long output (suggesting stacktrace was printed)
        exit.checkAssertionAfterwards(() -> {
            assertThat("Err out should not contain any stacktrace.",
                    sysErr.getLog().split(LINE_SEP),
                    arrayWithSize(lessThan(4)));
            assertThat("Sys out should not contain any stacktrace.",
                    sysOut.getLog().split(LINE_SEP),
                    arrayWithSize(lessThan(4)));
        });
        Path logPath = Paths.get("logs", "somelog.log").toAbsolutePath();

        // it should end with exit(1) and don't print whole stacktrace, just few lines describing the issue
        Main.main(new String[] {"this/obviously does not exist", logPath.toString()});
    }
}
