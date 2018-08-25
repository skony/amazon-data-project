package pl.put.fc.model.mongo;

import javax.xml.bind.annotation.XmlRootElement;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

@XmlRootElement
@Entity("category")
public class Category {
    
    @Id
    public String name;
    
    @Reference("parent_category_name")
    public Category parentCategory;
    
    public Category() {
    }
    
    public Category(String name) {
        this.name = name;
    }
    
    public Category(String name, Category parentCategory) {
        this.name = name;
        this.parentCategory = parentCategory;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Category getParentCategory() {
        return parentCategory;
    }
    
    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }
}
