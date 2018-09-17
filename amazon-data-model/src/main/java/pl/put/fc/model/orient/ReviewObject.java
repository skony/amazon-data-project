package pl.put.fc.model.orient;

import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OVertex;

@XmlRootElement
public class ReviewObject {
    
    @JsonProperty
    private int votedHelpful;
    
    @JsonProperty
    private int votedNotHelpful;
    
    @JsonProperty
    private String reviewText;
    
    @JsonProperty
    private double overall;
    
    @JsonProperty
    private String summary;
    
    @JsonProperty
    private long reviewTime;
    
    @JsonProperty
    private String product;
    
    @JsonProperty
    private ReviewerObject reviewer;
    
    public ReviewObject() {
    }
    
    public ReviewObject(OVertex vertex) {
        votedHelpful = vertex.getProperty(Review.VOTED_HELPFUL);
        votedNotHelpful = vertex.getProperty(Review.VOTED_NOT_HELPFUL);
        reviewText = vertex.getProperty(Review.REVIEW_TEXT);
        overall = vertex.getProperty(Review.OVERALL);
        summary = vertex.getProperty(Review.SUMMARY);
        reviewTime = vertex.getProperty(Review.REVIEW_TIME);
        reviewer = new ReviewerObject(vertex.getVertices(ODirection.OUT, Review.REVIEWER).iterator().next());
        product = vertex.getVertices(ODirection.OUT, Review.PRODUCT).iterator().next().getProperty(Product.ID);
    }
    
}
