package pl.put.fc.loader.boundary;

import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonNode;
import pl.put.fc.datamodel.ReviewDataRow;

public abstract class AbstractReviewDataLoader implements DataLoader {
    
    private static final String REVIEWER_ID = "reviewerID";
    private static final String PRODUCT_ID = "asin";
    private static final String REVIEWER_NAME = "reviewerName";
    private static final String HELPFUL = "helpful";
    private static final String REVIEWER_TEXT = "reviewText";
    private static final String OVERALL = "overall";
    private static final String SUMMARY = "summary";
    private static final String REVIEWER_TIME = "unixReviewTime";
    
    protected ReviewDataRow getRow(JsonNode node) {
        ReviewDataRow reviewDataRow = new ReviewDataRow();
        reviewDataRow.setReviewerId(node.path(REVIEWER_ID).asText());
        reviewDataRow.setProductId(node.path(PRODUCT_ID).asText());
        reviewDataRow.setReviewerName(node.path(REVIEWER_NAME).asText());
        Iterator<JsonNode> elementsOfHelpful = node.path(HELPFUL).elements();
        reviewDataRow.setHelpful_1(elementsOfHelpful.next().asInt());
        reviewDataRow.setHelpful_2(elementsOfHelpful.next().asInt());
        reviewDataRow.setReviewText(node.path(REVIEWER_TEXT).asText());
        reviewDataRow.setOverall(node.path(OVERALL).asDouble());
        reviewDataRow.setSummary(node.path(SUMMARY).asText());
        reviewDataRow.setUnixReviewTime(node.path(REVIEWER_TIME).asLong());
        return reviewDataRow;
    }
}
