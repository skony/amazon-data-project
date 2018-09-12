package pl.put.fc.loader.control.neo4j;

import org.neo4j.ogm.session.Session;
import com.fasterxml.jackson.databind.JsonNode;
import pl.put.fc.datamodel.ReviewDataRow;
import pl.put.fc.loader.boundary.AbstractReviewDataLoader;
import pl.put.fc.model.neo4j.Product;
import pl.put.fc.model.neo4j.Review;
import pl.put.fc.model.neo4j.Reviewer;

public class Neo4JReviewDataLoader extends AbstractReviewDataLoader {
    
    private Session session;
    
    public Neo4JReviewDataLoader(Session session) {
        this.session = session;
    }
    
    @Override
    public void loadEntitiesToDb(JsonNode node) {
        ReviewDataRow dataRow = getRow(node);
        Review review = new Review();
        
        Product product = session.load(Product.class, dataRow.getProductId());
        if (product == null) {
            return;
        }
        review.setProduct(product);
        
        Reviewer user = session.load(Reviewer.class, dataRow.getReviewerId());
        if (user == null) {
            user = new Reviewer(dataRow.getReviewerId(), dataRow.getReviewerName());
            session.save(user);
        }
        review.setReviewer(user);
        
        review.setVotedHelpful(dataRow.getHelpful_1());
        review.setVotedNotHelpful(dataRow.getHelpful_2() - dataRow.getHelpful_1());
        review.setReviewText(dataRow.getReviewText());
        review.setOverall(dataRow.getOverall());
        review.setSummary(dataRow.getSummary());
        review.setReviewTime(dataRow.getUnixReviewTime());
        
        session.save(review);
    }
    
    @Override
    public void loadRelationsToDb(JsonNode node) {
    }
    
    @Override
    public void beginTransaction() {
        session.beginTransaction();
    }
    
    @Override
    public void endTransaction() {
        session.getTransaction().commit();
    }
    
    @Override
    public int getNumberOfInsertsPerEntityTransaction() {
        return 0;
    }
    
    @Override
    public int getNumberOfInsertsPerRelationTransaction() {
        return 0;
    }
}
