package pl.put.fc.model.orient;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OVertex;

@XmlRootElement
public class ProductObject {
    
    @JsonProperty
    private String id;
    
    @JsonProperty
    private String title;
    
    @JsonProperty
    private double price;
    
    @JsonProperty
    private String brand;
    
    @JsonProperty
    private List<CategoryObject> categories = new ArrayList<>();
    
    public ProductObject() {
    }
    
    public ProductObject(OVertex vertex) {
        id = vertex.getProperty(Product.ID);
        title = vertex.getProperty(Product.TITLE);
        price = vertex.getProperty(Product.PRICE);
        brand = vertex.getProperty(Product.BRAND);
        vertex.getVertices(ODirection.OUT, Product.CATEGORY).forEach(category -> categories.add(new CategoryObject(category)));
    }
    
    public String getId() {
        return id;
    }
}
