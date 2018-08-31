package pl.put.fc.model.orient;

import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OVertex;

@XmlRootElement
public class Review {
    
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
    private Reviewer reviewer;
    
    public Review() {
    }
    
    public Review(OVertex vertex) {
        votedHelpful = vertex.getProperty(ReviewDefinition.VOTED_HELPFUL);
        votedNotHelpful = vertex.getProperty(ReviewDefinition.VOTED_NOT_HELPFUL);
        reviewText = vertex.getProperty(ReviewDefinition.REVIEW_TEXT);
        overall = vertex.getProperty(ReviewDefinition.OVERALL);
        summary = vertex.getProperty(ReviewDefinition.SUMMARY);
        reviewTime = vertex.getProperty(ReviewDefinition.REVIEW_TIME);
        reviewer = new Reviewer(vertex.getVertices(ODirection.OUT, ReviewDefinition.REVIEWER).iterator().next());
        product = vertex.getVertices(ODirection.OUT, ReviewDefinition.PRODUCT).iterator().next().getProperty(ProductDefinition.ID);
    }
    
}
