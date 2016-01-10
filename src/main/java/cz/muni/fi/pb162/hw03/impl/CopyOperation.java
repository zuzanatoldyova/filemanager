package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.Operation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 *
 * @author Zuzana Toldyova <323119@mail.muni.cz>
 */
public class CopyOperation implements Operation{
    
    private Path toPath;
    private String pattern;

    public CopyOperation(Path toPath, String pattern){
        this.toPath = toPath;
        this.pattern = pattern;
    }
    
    @Override
    public boolean execute(Path path) throws IOException {
        if (path.endsWith(pattern)) {
            Files.copy(path, toPath, StandardCopyOption.ATOMIC_MOVE);
            return true;
        }

        return false;
    }
    
    @Override
    public String toString(){
        return "CP";
    }
}
