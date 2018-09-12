package pl.put.fc.loader.control.orient;

import com.fasterxml.jackson.databind.JsonNode;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import pl.put.fc.datamodel.ReviewDataRow;
import pl.put.fc.loader.boundary.AbstractReviewDataLoader;
import pl.put.fc.model.orient.ReviewDefinition;
import pl.put.fc.model.orient.ReviewerDefinition;

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
        OVertex review = session.newVertex(ReviewDefinition.class.getSimpleName());
        OResultSet productQueryResult = session.query(QUERY_PRODUCT, dataRow.getProductId());
        if (!productQueryResult.hasNext()) {
            return;
        }
        review.addEdge(productQueryResult.next().getVertex().get(), ReviewDefinition.PRODUCT);
        
        OResultSet userQueryResult = session.query(QUERY_USER, dataRow.getReviewerId());
        if (!userQueryResult.hasNext()) {
            OVertex user = session.newVertex(ReviewerDefinition.class.getSimpleName());
            user.setProperty(ReviewerDefinition.ID, dataRow.getReviewerId());
            user.setProperty(ReviewerDefinition.NAME, dataRow.getReviewerName());
            session.save(user);
            review.addEdge(user, ReviewDefinition.REVIEWER);
        } else {
            review.addEdge(userQueryResult.next().getVertex().get(), ReviewDefinition.REVIEWER);
        }
        
        review.setProperty(ReviewDefinition.VOTED_HELPFUL, dataRow.getHelpful_1());
        review.setProperty(ReviewDefinition.VOTED_NOT_HELPFUL, dataRow.getHelpful_2() - dataRow.getHelpful_1());
        review.setProperty(ReviewDefinition.REVIEW_TEXT, dataRow.getReviewText());
        review.setProperty(ReviewDefinition.OVERALL, dataRow.getOverall());
        review.setProperty(ReviewDefinition.SUMMARY, dataRow.getSummary());
        review.setProperty(ReviewDefinition.REVIEW_TIME, dataRow.getUnixReviewTime());
        
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
}
