package pl.put.fc.loader.control.orient;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import pl.put.fc.DataFile;
import pl.put.fc.JsonToDbLoader;
import pl.put.fc.loader.boundary.DataLoadInvoker;
import pl.put.fc.model.orient.Category;
import pl.put.fc.model.orient.Product;
import pl.put.fc.model.orient.Review;
import pl.put.fc.model.orient.Reviewer;

public class OrientDataLoadInvoker implements DataLoadInvoker {
    
    private OrientDB orientDb;
    private ODatabaseSession session;
    
    @Override
    public void init() {
        orientDb = new OrientDB("remote:localhost/amazondb", "root", "root", OrientDBConfig.defaultConfig());
        session = orientDb.open("amazondb", "root", "root");
        createVertexClass(Product.class.getSimpleName());
        createVertexClass(Category.class.getSimpleName());
        createVertexClass(Review.class.getSimpleName());
        createVertexClass(Reviewer.class.getSimpleName());
        createEdgeClass(Product.CATEGORY);
        createEdgeClass(Category.PARENT_CATEGORY);
        createEdgeClass(Product.ALSO_BOUGHT);
        createEdgeClass(Product.ALSO_VIEWED);
        createEdgeClass(Product.BOUGHT_TOGETHER);
        createEdgeClass(Product.BUY_AFTER_VIEWING);
        createEdgeClass(Review.PRODUCT);
        createEdgeClass(Review.REVIEWER);
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
