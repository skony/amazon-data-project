package pl.put.fc.model.mongo;

import javax.xml.bind.annotation.XmlRootElement;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@Entity("reviewer")
public class Reviewer {
    
    @JsonProperty
    @Id
    private String id;
    
    @JsonProperty
    private String name;
    
    public Reviewer() {
    }
    
    public Reviewer(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
