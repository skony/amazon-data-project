package pl.put.fc.model.neo4j;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Review {
    
    @Id
    @GeneratedValue
    private long id;
    
    @Relationship
    private Reviewer reviewer;
    
    @Relationship
    private Product product;
    
    private int votedHelpful;
    
    private int votedNotHelpful;
    
    private String reviewText;
    
    private double overall;
    
    private String summary;
    
    private long reviewTime;
    
    public Review() {
    }
    
    public void setId(long id) {
        this.id = id;
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
