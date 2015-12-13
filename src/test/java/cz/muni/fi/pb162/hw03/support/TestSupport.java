package cz.muni.fi.pb162.hw03.support;

import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import cz.muni.fi.pb162.hw03.FileManager;

/**
 * Created by jludvice on 16.8.15.
 */
public class TestSupport extends ExternalResource {
    public static final Path WORK_SRC = Paths.get("work", "src").toAbsolutePath();
    public static final Path WORK_DEST = Paths.get("work", "dest").toAbsolutePath();
    public static final Path TEST_DATA = Paths.get("test_data").toAbsolutePath();
    public static final Path JOB_FILE_PATH = Paths.get("work", "jobfile.cmd").toAbsolutePath();

    private Path jobSrc;
    private NativeExecutor nativeExecutor;

    private TestSupport() {
        super();
    }

    /**
     * Factory method for {@link TestSupport}.
     *
     * @param jobName name of job to test
     * @return new test support instance
     */
    public static TestSupport forJob(String jobName) {
        TestSupport support = new TestSupport();
        support.jobSrc = subPath(WORK_SRC, jobName);
        support.nativeExecutor = createNativeExecutor();
        return support;
    }

    public void deleteWorkDirs() throws IOException {
        // delete folder with test data
        nativeExecutor.deleteRecursively(WORK_SRC.getParent());

        // delete logs
        nativeExecutor.deleteRecursively(Paths.get("logs"));
    }

    public void createWorkSrcDir() throws IOException {
        Files.createDirectories(WORK_SRC);
    }

    /**
     * copy test data into working folder, replace placeholders in job file
     *
     * @throws IOException
     */
    private void prepareTest() throws IOException {
        // copy data from test_data to work dir
        nativeExecutor.copyRecursively(subPath(TEST_DATA, getJobName()), WORK_SRC);

        // fill placeholders, provided files are UTF-8 encoded
        String jobFile = new String(Files.readAllBytes(subPath(TEST_DATA, "jobs", getJobName() + ".cmd")), FileManager.JOB_FILE_CHARSET);

        jobFile = jobFile.replace("<src>", jobSrc.toString()).replace("<dest>", WORK_DEST.toString()).replace("<>", File.separator);

        // write with whatever encoding is system default
        Files.write(JOB_FILE_PATH, jobFile.getBytes(FileManager.JOB_FILE_CHARSET), StandardOpenOption.CREATE);
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        init();
    }

    /**
     * Allow preparing test in other way than JUnit Rule
     * @throws IOException
     */
    public void init() throws IOException {
        deleteWorkDirs();
        createWorkSrcDir();
        prepareTest();
    }

    /**
     * Create platform specific process executor.
     *
     * @return
     */
    private static NativeExecutor createNativeExecutor() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows"))
            return new WindowsExecutor();
        else
            return new UnixExecutor();
    }

    /**
     * Path to job source (copy of {@code test_data/<job name>}.
     *
     * @return
     */
    public Path getJobSrc() {
        return jobSrc;
    }

    /**
     * Name of current job name.
     *
     * @return
     */
    public String getJobName() {
        return jobSrc.getFileName().toString();
    }

    public static Path subPath(Path parent, String... path) {
        return Paths.get(parent.toString(), path);
    }
}
