package cz.muni.fi.pb162.hw03;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Hint - You may want to use this interface, if you decide to implement operations with Command design patter.
 * 
 * @see <a href="https://dzone.com/articles/design-patterns-command">Command design pattern</a>
 * @author Josef Ludvicek
 */
public interface Operation {

    /**
     * Execute operation on given path. Command is executed if path matches command's pattern, nothing happens
     * otherwise.
     *
     * @param path path to file on filesystem
     * @return true if pattern matches path and command was executed, false if pattern didn't match.
     * @throws IOException if command execution failed
     */
    boolean execute(Path path) throws IOException;
}
