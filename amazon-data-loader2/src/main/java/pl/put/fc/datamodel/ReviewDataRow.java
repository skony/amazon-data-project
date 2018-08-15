package pl.put.fc.datamodel;

public class ReviewDataRow {
    
    private String reviewerId;
    private String productId;;
    private String reviewerName;
    private int helpful_1;
    private int helpful_2;
    private String reviewText;
    private double overall;
    private String summary;
    private long unixReviewTime;
    
    public ReviewDataRow() {
        
    }
    
    public ReviewDataRow(String reviewerId, String productId, String reviewerName, int helpful, String reviewText, double overall,
            String summary, long unixReviewTime) {
        this.reviewerId = reviewerId;
        this.productId = productId;
        this.reviewerName = reviewerName;
        helpful_1 = helpful;
        this.reviewText = reviewText;
        this.overall = overall;
        this.summary = summary;
        this.unixReviewTime = unixReviewTime;
    }
    
    public String getReviewerId() {
        return reviewerId;
    }
    
    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getReviewerName() {
        return reviewerName;
    }
    
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    
    public String getReviewText() {
        return reviewText;
    }
    
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
    
    public int getHelpful_1() {
        return helpful_1;
    }
    
    public void setHelpful_1(int helpful_1) {
        this.helpful_1 = helpful_1;
    }
    
    public int getHelpful_2() {
        return helpful_2;
    }
    
    public void setHelpful_2(int helpful_2) {
        this.helpful_2 = helpful_2;
    }
    
    public double getOverall() {
        return overall;
    }
    
    public void setOverall(double overall) {
        this.overall = overall;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public long getUnixReviewTime() {
        return unixReviewTime;
    }
    
    public void setUnixReviewTime(long unixReviewTime) {
        this.unixReviewTime = unixReviewTime;
    }
}
