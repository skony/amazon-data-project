package pl.put.fc.model.orient;

import java.util.Iterator;
import java.util.Optional;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OVertex;

@XmlRootElement
public class CategoryObject {
    
    @JsonProperty
    private String name;
    
    @JsonProperty
    private CategoryObject parentCategory;
    
    public CategoryObject() {
    }
    
    public CategoryObject(OVertex vertex) {
        name = vertex.getProperty(Category.NAME);
        Optional<OVertex> parentCategory = getParentCategory(vertex);
        if (parentCategory.isPresent()) {
            this.parentCategory = new CategoryObject(parentCategory.get());
        }
    }
    
    private Optional<OVertex> getParentCategory(OVertex vertex) {
        Iterator<OVertex> iterator = vertex.getVertices(ODirection.OUT, Category.PARENT_CATEGORY).iterator();
        if (iterator.hasNext()) {
            return Optional.of(iterator.next());
        }
        return Optional.empty();
    }
}
