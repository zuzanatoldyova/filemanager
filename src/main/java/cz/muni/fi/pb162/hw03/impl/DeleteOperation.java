package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.Operation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Zuzana Toldyova <323119@mail.muni.cz>
 */
public class DeleteOperation implements Operation{
    
    private String pattern;
    
    public DeleteOperation(String pattern){
        this.pattern = pattern;
    }

    @Override
    public boolean execute(Path path) throws IOException {
        if (path.endsWith(pattern)){
            Files.delete(path);
            return true;
        }
        
        return false;
    }
    
    @Override
    public String toString(){
        return "DEL";
    }
}
