package pl.put.fc.model.mongo;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("reviewer")
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
