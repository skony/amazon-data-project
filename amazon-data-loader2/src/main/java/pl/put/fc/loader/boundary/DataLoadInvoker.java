package pl.put.fc.loader.boundary;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import pl.put.fc.file.DataFile;

public interface DataLoadInvoker {
    
    void init();
    
    void invoke(DataFile dataFile) throws JsonProcessingException, IOException;
    
    void close();
}
