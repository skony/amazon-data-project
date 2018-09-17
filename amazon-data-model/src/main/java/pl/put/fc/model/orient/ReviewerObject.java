package pl.put.fc.model.orient;

import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orientechnologies.orient.core.record.OVertex;

@XmlRootElement
public class ReviewerObject {
    
    @JsonProperty
    private String id;
    
    @JsonProperty
    private String name;
    
    public ReviewerObject() {
    }
    
    public ReviewerObject(OVertex vertex) {
        id = vertex.getProperty(Reviewer.ID);
        name = vertex.getProperty(Reviewer.NAME);
    }
}
