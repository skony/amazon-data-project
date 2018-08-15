package pl.put.fc.loader.control.postgres;

import java.io.IOException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import pl.put.fc.DataFile;
import pl.put.fc.JsonToDbLoader;
import pl.put.fc.loader.boundary.DataLoadInvoker;

public class PostgresDataLoadInvoker implements DataLoadInvoker {
    
    private SessionFactory sessionFactory;
    private Session session;
    
    @Override
    public void init() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
        session = sessionFactory.openSession();
    }
    
    @Override
    public void invoke(DataFile dataFile) throws JsonProcessingException, IOException {
        JsonToDbLoader jsonToDbLoader = new JsonToDbLoader();
        jsonToDbLoader.load(dataFile.getFixedMetaFile(), new PostgresMetaDataLoader(session));
        jsonToDbLoader.load(dataFile.getReviewFile(), new PostgresReviewDataLoader(session));
    }
    
    @Override
    public void close() {
        session.close();
        sessionFactory.close();
    }
}
