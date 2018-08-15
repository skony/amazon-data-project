package pl.put.fc.model.neo4j;

import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
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
