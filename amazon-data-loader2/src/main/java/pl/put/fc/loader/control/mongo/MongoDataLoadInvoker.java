package pl.put.fc.loader.control.mongo;

import java.io.IOException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import pl.put.fc.DataFile;
import pl.put.fc.JsonToDbLoader;
import pl.put.fc.loader.boundary.DataLoadInvoker;

public class MongoDataLoadInvoker implements DataLoadInvoker {
    
    private Morphia morphia;
    private Datastore datastore;
    
    @Override
    public void init() {
        morphia = new Morphia();
        morphia.mapPackage("pl.put.fc.model.mongo");
        datastore = morphia.createDatastore(new MongoClient(), "amazondb");
        datastore.ensureIndexes();
    }
    
    @Override
    public void invoke(DataFile dataFile) throws JsonProcessingException, IOException {
        JsonToDbLoader jsonToDbLoader = new JsonToDbLoader();
        jsonToDbLoader.load(dataFile.getFixedMetaFile(), new MongoMetaDataLoader(datastore));
        jsonToDbLoader.load(dataFile.getReviewFile(), new MongoReviewDataLoader(datastore));
    }
    
    @Override
    public void close() {
        
    }
}
