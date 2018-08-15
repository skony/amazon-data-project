package pl.put.fc.model.neo4j;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Category {
    
    @Id
    private String name;
    
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
}
