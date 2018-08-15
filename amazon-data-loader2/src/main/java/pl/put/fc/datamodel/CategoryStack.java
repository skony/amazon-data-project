package pl.put.fc.datamodel;

import java.util.LinkedList;
import java.util.List;

public class CategoryStack {
    
    LinkedList<String> categories;
    
    public CategoryStack(LinkedList<String> categories) {
        this.categories = categories;
    }
    
    public List<String> getCategories() {
        return categories;
    }
    
    public void setCategories(LinkedList<String> categories) {
        this.categories = categories;
    }
}
