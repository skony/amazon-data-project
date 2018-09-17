package pl.put.fc;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import pl.put.fc.file.DataFile;
import pl.put.fc.fixer.MetaFileFixer;
import pl.put.fc.loader.boundary.DataLoadInvoker;
import pl.put.fc.loader.control.mongo.MongoDataLoadInvoker;

public class LoaderApplication {
    
    private static final List<DataFile> DATA_FILES = Arrays.asList(DataFile.MUSICAL_INSTRUMENTS);
    private static final List<DataLoadInvoker> DATA_LOAD_INVOKERS =
            // Arrays.asList(new PostgresDataLoadInvoker(), new MongoDataLoadInvoker(), new OrientDataLoadInvoker(),
            // new Neo4JDataLoadInvoker());
            Arrays.asList(new MongoDataLoadInvoker());
    
    public static void main(String[] args) throws JsonProcessingException, IOException {
        MetaFileFixer metaFileFixer = new MetaFileFixer();
        DATA_FILES.forEach(file -> proceedFile(metaFileFixer, file));
    }
    
    private static void proceedFile(MetaFileFixer metaFileFixer, DataFile file) {
        try {
            if (!metaFileFixer.isAlreadyFixed(file.getFixedMetaFile())) {
                metaFileFixer.fix(file.getOriginalMetaFile());
            }
            DATA_LOAD_INVOKERS.forEach(invoker -> preceedFile(invoker, file));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    private static void preceedFile(DataLoadInvoker invoker, DataFile file) {
        try {
            invoker.init();
            invoker.invoke(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            invoker.close();
        }
    }
}
