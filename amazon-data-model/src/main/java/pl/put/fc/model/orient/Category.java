package pl.put.fc.model.orient;

import java.util.Iterator;
import java.util.Optional;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orientechnologies.orient.core.record.ODirection;
import com.orientechnologies.orient.core.record.OVertex;

@XmlRootElement
public class Category {
    
    @JsonProperty
    private String name;
    
    @JsonProperty
    private Category parentCategory;
    
    public Category() {
    }
    
    public Category(OVertex vertex) {
        name = vertex.getProperty(CategoryDefinition.NAME);
        Optional<OVertex> parentCategory = getParentCategory(vertex);
        if (parentCategory.isPresent()) {
            this.parentCategory = new Category(parentCategory.get());
        }
    }
    
    private Optional<OVertex> getParentCategory(OVertex vertex) {
        Iterator<OVertex> iterator = vertex.getVertices(ODirection.OUT, CategoryDefinition.PARENT_CATEGORY).iterator();
        if (iterator.hasNext()) {
            return Optional.of(iterator.next());
        }
        return Optional.empty();
    }
}
