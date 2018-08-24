package pl.put.fc.model.postgres;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@Entity
@Table(name = "reviewer")
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
