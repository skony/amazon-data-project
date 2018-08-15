package pl.put.fc.model.postgres;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reviewer")
public class Reviewer {
    
    @Id
    private String id;
    private String name;
    
    public Reviewer() {
    }
    
    public Reviewer(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
