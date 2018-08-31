package pl.put.fc.model.orient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OVertex;

@XmlRootElement
public class Product {
    
    @JsonProperty
    private String id;
    
    @JsonProperty
    private String title;
    
    @JsonProperty
    private double price;
    
    @JsonProperty
    private String brand;
    
    @JsonProperty
    private List<Category> categories = new ArrayList<>();
    
    public Product() {
    }
    
    public Product(OVertex vertex) {
        id = vertex.getProperty(ProductDefinition.ID);
        title = vertex.getProperty(ProductDefinition.TITLE);
        price = vertex.getProperty(ProductDefinition.PRICE);
        brand = vertex.getProperty(ProductDefinition.BRAND);
        vertex.getVertices(ODirection.OUT, ProductDefinition.CATEGORY).forEach(category -> categories.add(new Category(category)));
    }
    
    public String getId() {
        return id;
    }
}
