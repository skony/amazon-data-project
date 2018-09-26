package pl.put.fc.loader.control.mongo;

import org.mongodb.morphia.Datastore;
import com.fasterxml.jackson.databind.JsonNode;
import pl.put.fc.datamodel.ReviewDataRow;
import pl.put.fc.loader.boundary.AbstractReviewDataLoader;
import pl.put.fc.model.mongo.Product;
import pl.put.fc.model.mongo.Review;
import pl.put.fc.model.mongo.Reviewer;

public class MongoReviewDataLoader extends AbstractReviewDataLoader {
    
    private Datastore datastore;
    
    public MongoReviewDataLoader(Datastore datastore) {
        this.datastore = datastore;
    }
    
    @Override
    public void loadEntitiesToDb(JsonNode node) {
        ReviewDataRow dataRow = getRow(node);
        Review review = new Review();
        
        Product product = datastore.get(Product.class, dataRow.getProductId());
        if (product == null) {
            return;
        }
        review.setProduct(product);
        
        Reviewer user = datastore.get(Reviewer.class, dataRow.getReviewerId());
        if (user == null) {
            user = new Reviewer(dataRow.getReviewerId(), dataRow.getReviewerName());
            datastore.save(user);
        }
        review.setReviewer(user);
        
        review.setVotedHelpful(dataRow.getHelpful_1());
        review.setVotedNotHelpful(dataRow.getHelpful_2() - dataRow.getHelpful_1());
        review.setReviewText(dataRow.getReviewText());
        review.setOverall(dataRow.getOverall());
        review.setSummary(dataRow.getSummary());
        review.setReviewTime(dataRow.getUnixReviewTime());
        
        datastore.save(review);
    }
    
    @Override
    public void loadRelationsToDb(JsonNode node) {
    }
    
    @Override
    public void beginTransaction() {
    }
    
    @Override
    public void endTransaction() {
    }
    
    @Override
    public int getNumberOfInsertsPerEntityTransaction() {
        return 5000;
    }
    
    @Override
    public int getNumberOfInsertsPerRelationTransaction() {
        return 5000;
    }
}
