package pl.put.fc.model.neo4j;

import java.util.List;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Product {
    
    @Id
    private String id;
    
    private String title;
    
    private double price;
    
    private String brand;
    
    @Relationship
    private List<Category> categories;
    
    @Relationship
    private List<Product> alsoBought;
    
    @Relationship
    private List<Product> alsoViewed;
    
    @Relationship
    private List<Product> boughtTogether;
    
    @Relationship
    private List<Product> buyAfterViewing;
    
    public Product() {
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    
    public void setAlsoBought(List<Product> alsoBought) {
        this.alsoBought = alsoBought;
    }
    
    public void setAlsoViewed(List<Product> alsoViewed) {
        this.alsoViewed = alsoViewed;
    }
    
    public void setBoughtTogether(List<Product> boughtTogether) {
        this.boughtTogether = boughtTogether;
    }
    
    public void setBuyAfterViewing(List<Product> buyAfterViewing) {
        this.buyAfterViewing = buyAfterViewing;
    }
}
