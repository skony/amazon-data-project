package pl.put.fc.loader.control.orient;

import com.fasterxml.jackson.databind.JsonNode;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import pl.put.fc.datamodel.ReviewDataRow;
import pl.put.fc.loader.boundary.AbstractReviewDataLoader;
import pl.put.fc.model.orient.Review;
import pl.put.fc.model.orient.Reviewer;

public class OrientReviewDataLoader extends AbstractReviewDataLoader {
    
    private static final String QUERY_PRODUCT = "SELECT FROM Product WHERE id = ?";
    private static final String QUERY_USER = "SELECT FROM Reviewer WHERE id = ?";
    
    private ODatabaseSession session;
    
    public OrientReviewDataLoader(ODatabaseSession session) {
        this.session = session;
    }
    
    @Override
    public void loadEntitiesToDb(JsonNode node) {
        ReviewDataRow dataRow = getRow(node);
        OVertex review = session.newVertex(Review.class.getSimpleName());
        OResultSet productQueryResult = session.query(QUERY_PRODUCT, dataRow.getProductId());
        if (!productQueryResult.hasNext()) {
            return;
        }
        review.addEdge(productQueryResult.next().getVertex().get(), Review.PRODUCT);
        
        OResultSet userQueryResult = session.query(QUERY_USER, dataRow.getReviewerId());
        if (!userQueryResult.hasNext()) {
            OVertex user = session.newVertex(Reviewer.class.getSimpleName());
            user.setProperty(Reviewer.ID, dataRow.getReviewerId());
            user.setProperty(Reviewer.NAME, dataRow.getReviewerName());
            session.save(user);
            review.addEdge(user, Review.REVIEWER);
        } else {
            review.addEdge(userQueryResult.next().getVertex().get(), Review.REVIEWER);
        }
        
        review.setProperty(Review.VOTED_HELPFUL, dataRow.getHelpful_1());
        review.setProperty(Review.VOTED_NOT_HELPFUL, dataRow.getHelpful_2() - dataRow.getHelpful_1());
        review.setProperty(Review.REVIEW_TEXT, dataRow.getReviewText());
        review.setProperty(Review.OVERALL, dataRow.getOverall());
        review.setProperty(Review.SUMMARY, dataRow.getSummary());
        review.setProperty(Review.REVIEW_TIME, dataRow.getUnixReviewTime());
        
        session.save(review);
    }
    
    @Override
    public void loadRelationsToDb(JsonNode node) {
    }
    
    @Override
    public void beginTransaction() {
        session.begin();
    }
    
    @Override
    public void endTransaction() {
        session.commit();
    }
    
    @Override
    public int getNumberOfInsertsPerEntityTransaction() {
        return 25;
    }
    
    @Override
    public int getNumberOfInsertsPerRelationTransaction() {
        return Integer.MAX_VALUE;
    }
}
