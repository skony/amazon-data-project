package pl.put.fc.datamodel;

import java.util.List;

public class MetaDataRow {
    
    private String productId;
    private String title;
    private double price;
    private String imgUrl;
    private List<String> alsoBought;
    private List<String> alsoViewed;
    private List<String> boughtTogether;
    private List<String> buyAfterViewing;
    private String brand;
    private String salesRank;
    private List<CategoryStack> categories;
    
    public MetaDataRow(String productId, String title, double price, String imgUrl, List<String> alsoBought, List<String> alsoViewed,
            List<String> boughtTogether, List<String> buyAfterViewing, String brand, String salesRank, List<CategoryStack> categories) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.imgUrl = imgUrl;
        this.alsoBought = alsoBought;
        this.alsoViewed = alsoViewed;
        this.boughtTogether = boughtTogether;
        this.buyAfterViewing = buyAfterViewing;
        this.brand = brand;
        this.salesRank = salesRank;
        this.categories = categories;
    }
    
    public MetaDataRow() {
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getImgUrl() {
        return imgUrl;
    }
    
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    
    public List<String> getAlsoBought() {
        return alsoBought;
    }
    
    public void setAlsoBought(List<String> alsoBought) {
        this.alsoBought = alsoBought;
    }
    
    public List<String> getAlsoViewed() {
        return alsoViewed;
    }
    
    public void setAlsoViewed(List<String> alsoViewed) {
        this.alsoViewed = alsoViewed;
    }
    
    public List<String> getBoughtTogether() {
        return boughtTogether;
    }
    
    public void setBoughtTogether(List<String> boughtTogether) {
        this.boughtTogether = boughtTogether;
    }
    
    public List<String> getBuyAfterViewing() {
        return buyAfterViewing;
    }
    
    public void setBuyAfterViewing(List<String> buyAfterViewing) {
        this.buyAfterViewing = buyAfterViewing;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getSalesRank() {
        return salesRank;
    }
    
    public void setSalesRank(String salesRank) {
        this.salesRank = salesRank;
    }
    
    public List<CategoryStack> getCategories() {
        return categories;
    }
    
    public void setCategories(List<CategoryStack> categories) {
        this.categories = categories;
    }
}
