package pl.put.fc.model.orient;

import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orientechnologies.orient.core.record.OVertex;

@XmlRootElement
public class Reviewer {
    
    @JsonProperty
    private String id;
    
    @JsonProperty
    private String name;
    
    public Reviewer() {
    }
    
    public Reviewer(OVertex vertex) {
        id = vertex.getProperty(ReviewerDefinition.ID);
        name = vertex.getProperty(ReviewerDefinition.NAME);
    }
}
