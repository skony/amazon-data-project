package pl.put.fc.loader.control.postgres;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.fasterxml.jackson.databind.JsonNode;
import pl.put.fc.datamodel.ReviewDataRow;
import pl.put.fc.loader.boundary.AbstractReviewDataLoader;
import pl.put.fc.model.postgres.Product;
import pl.put.fc.model.postgres.Review;
import pl.put.fc.model.postgres.Reviewer;

public class PostgresReviewDataLoader extends AbstractReviewDataLoader {
    
    private Session session;
    private Transaction transaction;
    
    public PostgresReviewDataLoader(Session session) {
        this.session = session;
    }
    
    @Override
    public void loadEntitiesToDb(JsonNode node) {
        ReviewDataRow dataRow = getRow(node);
        Review review = new Review();
        
        Product product = session.find(Product.class, dataRow.getProductId());
        if (product == null) {
            return;
        }
        review.setProduct(product);
        
        Reviewer user = session.find(Reviewer.class, dataRow.getReviewerId());
        if (user == null) {
            user = new Reviewer(dataRow.getReviewerId(), dataRow.getReviewerName());
            session.persist(user);
        }
        review.setReviewer(user);
        
        review.setVotedHelpful(dataRow.getHelpful_1());
        review.setVotedNotHelpful(dataRow.getHelpful_2() - dataRow.getHelpful_1());
        review.setReviewText(dataRow.getReviewText());
        review.setOverall(dataRow.getOverall());
        review.setSummary(dataRow.getSummary());
        review.setReviewTime(dataRow.getUnixReviewTime());
        
        session.persist(review);
    }
    
    @Override
    public void loadRelationsToDb(JsonNode node) {
    }
    
    @Override
    public void beginTransaction() {
        transaction = session.beginTransaction();
    }
    
    @Override
    public void endTransaction() {
        transaction.commit();
    }
}
