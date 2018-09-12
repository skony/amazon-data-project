package pl.put.fc.loader.control.neo4j;

import java.io.IOException;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import pl.put.fc.file.DataFile;
import pl.put.fc.loader.boundary.DataLoadInvoker;
import pl.put.fc.loader.control.JsonToDbLoader;

public class Neo4JDataLoadInvoker implements DataLoadInvoker {
    
    private Configuration configuration;
    private SessionFactory sessionFactory;
    private Session session;
    
    @Override
    public void init() {
        configuration = new Configuration.Builder().uri("bolt://neo4j:root@localhost").build();
        sessionFactory = new SessionFactory(configuration, "pl.put.fc.model.neo4j");
        session = sessionFactory.openSession();
    }
    
    @Override
    public void invoke(DataFile dataFile) throws JsonProcessingException, IOException {
        JsonToDbLoader jsonToDbLoader = new JsonToDbLoader();
        // jsonToDbLoader.load(dataFile.getFixedMetaFile(), new Neo4JMetaDataLoader(session));
        jsonToDbLoader.load(dataFile.getReviewFile(), new Neo4JReviewDataLoader(session));
    }
    
    @Override
    public void close() {
        session.clear();
        sessionFactory.close();
    }
    
}
