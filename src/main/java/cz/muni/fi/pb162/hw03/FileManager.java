package cz.muni.fi.pb162.hw03;

import java.nio.charset.Charset;

/**
 * @author Josef Ludvicek
 */
public interface FileManager {

    /**
     * Job files in tests are written using utf-8 encoding
     */
    Charset JOB_FILE_CHARSET = Charset.forName("UTF-8");

    /**
     * Execute job operation specified in {@code jobPath}. Atomicity of job execution is NOT guaranteed. It may fail
     * during the execution. <br/>
     * However all file modifications must be included in log file.
     *
     * @param jobPath path to job file
     * @param logFilePath path where log file should be written
     * @throws Exception Throw appropriate exception in case of error. Feel free to define and throw own exceptions.
     */
    void executeJob(String jobPath, String logFilePath) throws Exception;
}
