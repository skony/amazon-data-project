package pl.put.fc.loader.control.orient;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import pl.put.fc.file.DataFile;
import pl.put.fc.loader.boundary.DataLoadInvoker;
import pl.put.fc.loader.control.JsonToDbLoader;
import pl.put.fc.model.orient.CategoryDefinition;
import pl.put.fc.model.orient.ProductDefinition;
import pl.put.fc.model.orient.ReviewDefinition;
import pl.put.fc.model.orient.ReviewerDefinition;

public class OrientDataLoadInvoker implements DataLoadInvoker {
    
    private OrientDB orientDb;
    private ODatabaseSession session;
    
    @Override
    public void init() {
        orientDb = new OrientDB("remote:localhost/amazondb", "root", "root", OrientDBConfig.defaultConfig());
        session = orientDb.open("amazondb", "root", "root");
        createVertexClass(ProductDefinition.class.getSimpleName());
        createVertexClass(CategoryDefinition.class.getSimpleName());
        createVertexClass(ReviewDefinition.class.getSimpleName());
        createVertexClass(ReviewerDefinition.class.getSimpleName());
        createEdgeClass(ProductDefinition.CATEGORY);
        createEdgeClass(CategoryDefinition.PARENT_CATEGORY);
        createEdgeClass(ProductDefinition.ALSO_BOUGHT);
        createEdgeClass(ProductDefinition.ALSO_VIEWED);
        createEdgeClass(ProductDefinition.BOUGHT_TOGETHER);
        createEdgeClass(ProductDefinition.BUY_AFTER_VIEWING);
        createEdgeClass(ReviewDefinition.PRODUCT);
        createEdgeClass(ReviewDefinition.REVIEWER);
    }
    
    @Override
    public void invoke(DataFile dataFile) throws JsonProcessingException, IOException {
        JsonToDbLoader jsonToDbLoader = new JsonToDbLoader();
        jsonToDbLoader.load(dataFile.getFixedMetaFile(), new OrientMetaDataLoader(session));
        jsonToDbLoader.load(dataFile.getReviewFile(), new OrientReviewDataLoader(session));
    }
    
    @Override
    public void close() {
        session.close();
        orientDb.close();
    }
    
    private void createVertexClass(String name) {
        try {
            session.createVertexClass(name);
        } catch (Exception e) {
            
        }
    }
    
    private void createEdgeClass(String name) {
        try {
            session.createEdgeClass(name);
        } catch (Exception e) {
            
        }
    }
}
