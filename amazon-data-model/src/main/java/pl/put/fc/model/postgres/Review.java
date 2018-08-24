package pl.put.fc.model.postgres;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Piotr Skonieczny
 */
@XmlRootElement
@Entity
@Table(name = "review")
public class Review {
    
    @JsonProperty
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @JsonProperty
    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private Reviewer reviewer;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    @JsonProperty
    @Column(name = "voted_helpful")
    private int votedHelpful;
    
    @JsonProperty
    @Column(name = "voted_not_helpful")
    private int votedNotHelpful;
    
    @JsonProperty
    @Column(name = "review_text", length = 32767)
    private String reviewText;
    
    @JsonProperty
    private double overall;
    
    @JsonProperty
    @Column(length = 1023)
    private String summary;
    
    @JsonProperty
    @Column(name = "review_time")
    private long reviewTime;
    
    public Review() {
    }
    
    public Review(long id, Reviewer reviewer, Product product, int votedHelpful, int votedNotHelpful, String reviewText, double overall,
            String summary, long reviewTime) {
        this.id = id;
        this.reviewer = reviewer;
        this.product = product;
        this.votedHelpful = votedHelpful;
        this.votedNotHelpful = votedNotHelpful;
        this.reviewText = reviewText;
        this.overall = overall;
        this.summary = summary;
        this.reviewTime = reviewTime;
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
