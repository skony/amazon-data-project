package pl.put.fc.model.neo4j;

import javax.xml.bind.annotation.XmlRootElement;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@NodeEntity
public class Category {
    
    @JsonProperty
    @Id
    private String name;
    
    @JsonProperty
    @Relationship
    private Category parentCategory;
    
    public Category() {
    }
    
    public Category(String name, Category parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
    }
    
    public Category(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
}
