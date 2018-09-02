package pl.put.fc.model.neo4j;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@NodeEntity
public class Review {
    
    @JsonProperty("id")
    @Id
    @GeneratedValue
    private Long uid;
    
    @JsonProperty
    @Relationship(type = "REVIEW_REVIEWER")
    private Reviewer reviewer;
    
    @Relationship(type = "REVIEW_PRODUCT")
    private Product product;
    
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
    
    public Review() {
    }
    
    public static Review copyFromQuery(Map<String, Object> queryResult) {
        Review review = (Review) queryResult.get("review");
        review.setProduct((Product) queryResult.get("product"));
        review.setReviewer((Reviewer) queryResult.get("reviewer"));
        return review;
    }
    
    @JsonGetter("product")
    public String getProductId() {
        return product.getId();
    }
    
    public void setId(long id) {
        uid = id;
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
