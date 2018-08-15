package pl.put.fc.model.postgres;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
    
    @Id
    public String name;
    
    @ManyToOne
    @JoinColumn(name = "parent_category_name")
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
