package pl.put.fc.model.mongo;

import javax.xml.bind.annotation.XmlRootElement;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Piotr Skonieczny
 */
@XmlRootElement
@Entity("review")
public class Review {
    
    @JsonProperty
    @Id
    private ObjectId id;
    
    @JsonProperty
    @Reference
    private Reviewer reviewer;
    
    @Reference
    private Product product;
    
    @JsonProperty
    @Property("voted_helpful")
    private int votedHelpful;
    
    @JsonProperty
    @Property("voted_not_helpful")
    private int votedNotHelpful;
    
    @JsonProperty
    @Property("review_text")
    private String reviewText;
    
    @JsonProperty
    private double overall;
    
    @JsonProperty
    private String summary;
    
    @JsonProperty
    @Property("review_time")
    private long reviewTime;
    
    public Review() {
    }
    
    public Review(Reviewer reviewer, Product product, int votedHelpful, int votedNotHelpful, String reviewText, double overall,
            String summary, long reviewTime) {
        reviewer = reviewer;
        this.product = product;
        this.votedHelpful = votedHelpful;
        this.votedNotHelpful = votedNotHelpful;
        this.reviewText = reviewText;
        this.overall = overall;
        this.summary = summary;
        this.reviewTime = reviewTime;
    }
    
    @JsonGetter("id")
    public String getId() {
        return id.toString();
    }
    
    @JsonGetter("product")
    public String getProductId() {
        return product.getId();
    }
    
    public void setReviewer(Reviewer reviewer) {
        this.reviewer = reviewer;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public void setVotedHelpful(int votedHelpful) {
        this.votedHelpful = votedHelpful;
    }
    
    public void setVotedNotHelpful(int votedNotHelpful) {
        this.votedNotHelpful = votedNotHelpful;
    }
    
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    
    public void setOverall(double overall) {
        this.overall = overall;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public void setReviewTime(long reviewTime) {
        this.reviewTime = reviewTime;
    }
}
