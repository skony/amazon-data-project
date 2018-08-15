package pl.put.fc.model.mongo;

import java.util.List;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

/**
 * @author Piotr Skonieczny
 */
@Entity("product")
public class Product {
    
    @Id
    private String id;
    
    private String title;
    
    private double price;
    
    private String brand;
    
    // private String description;
    
    @Reference
    private List<Category> categories;
    
    @Reference
    private List<Product> alsoBought;
    
    @Reference
    private List<Product> alsoViewed;
    
    @Reference
    private List<Product> boughtTogether;
    
    @Reference
    private List<Product> buyAfterViewing;
    
    public Product() {
    }
    
    public Product(String title, double price, String brand, List<Category> category, List<Product> alsoBought, List<Product> alsoViewed,
            List<Product> boughtTogether, List<Product> buyAfterViewing) {
        this.title = title;
        this.price = price;
        this.brand = brand;
        // this.category = category;
        this.alsoBought = alsoBought;
        this.alsoViewed = alsoViewed;
        this.boughtTogether = boughtTogether;
        this.buyAfterViewing = buyAfterViewing;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public List<Category> getCategory() {
        return categories;
    }
    
    public void setCategory(List<Category> category) {
        categories = category;
    }
    
    public List<Product> getAlsoBought() {
        return alsoBought;
    }
    
    public void setAlsoBought(List<Product> alsoBought) {
        this.alsoBought = alsoBought;
    }
    
    public List<Product> getAlsoViewed() {
        return alsoViewed;
    }
    
    public void setAlsoViewed(List<Product> alsoViewed) {
        this.alsoViewed = alsoViewed;
    }
    
    public List<Product> getBoughtTogether() {
        return boughtTogether;
    }
    
    public void setBoughtTogether(List<Product> boughtTogether) {
        this.boughtTogether = boughtTogether;
    }
    
    public List<Product> getBuyAfterViewing() {
        return buyAfterViewing;
    }
    
    public void setBuyAfterViewing(List<Product> buyAfterViewing) {
        this.buyAfterViewing = buyAfterViewing;
    }
}
