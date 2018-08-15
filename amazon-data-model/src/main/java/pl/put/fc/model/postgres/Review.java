package pl.put.fc.model.postgres;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Piotr Skonieczny
 */
@Entity
@Table(name = "review")
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private Reviewer reviewer;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @Column(name = "voted_helpful")
    private int votedHelpful;
    
    @Column(name = "voted_not_helpful")
    private int votedNotHelpful;
    
    @Column(name = "review_text", length = 32767)
    private String reviewText;
    
    private double overall;
    
    @Column(length = 1023)
    private String summary;
    
    @Column(name = "review_time")
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
