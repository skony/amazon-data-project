package pl.put.fc.model.neo4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@NodeEntity
public class Product {
    
    @JsonProperty("id")
    @Id
    private String uid;
    
    @JsonProperty
    private String title;
    
    @JsonProperty
    private double price;
    
    @JsonProperty
    private String brand;
    
    @JsonProperty
    @Relationship(type = "PRODUCT_CATEGORY")
    private List<Category> categories = new ArrayList<>();
    
    @JsonIgnore
    @Relationship
    private List<Product> alsoBought;
    
    @JsonIgnore
    @Relationship
    private List<Product> alsoViewed;
    
    @JsonIgnore
    @Relationship
    private List<Product> boughtTogether;
    
    @JsonIgnore
    @Relationship
    private List<Product> buyAfterViewing;
    
    public Product() {
    }
    
    public static List<Product> copyFromQuery(Iterable<Map<String, Object>> queryResults) {
        Set<Product> products = new HashSet<>();
        queryResults.forEach(result -> {
            Product product = (Product) result.get("product");
            Category category = (Category) result.get("category");
            product.categories.add(category);
            products.add(product);
        });
        return new ArrayList<>(products);
    }
    
    public void setId(String id) {
        uid = id;
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
    
    public String getId() {
        return uid;
    }
}
