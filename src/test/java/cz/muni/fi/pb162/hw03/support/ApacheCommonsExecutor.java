package cz.muni.fi.pb162.hw03.support;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Uses Apache commons IO library for recursive copy and delete.
 *
 * @author Josef Ludvicek
 */
public class ApacheCommonsExecutor implements NativeExecutor {
	@Override
	public void deleteRecursively(Path p) throws IOException {
		FileUtils.deleteDirectory(p.toFile());
	}

	@Override
	public void copyRecursively(Path from, Path to) throws IOException {
		FileUtils.copyDirectoryToDirectory(from.toFile(), to.toFile());
	}
}
