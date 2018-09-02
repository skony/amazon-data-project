package pl.put.fc.model.neo4j;

import javax.xml.bind.annotation.XmlRootElement;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@NodeEntity
public class Reviewer {
    
    @JsonProperty("id")
    @Id
    private String uid;
    
    @JsonProperty
    private String name;
    
    public Reviewer() {
    }
    
    public Reviewer(String id, String name) {
        uid = id;
        this.name = name;
    }
}
