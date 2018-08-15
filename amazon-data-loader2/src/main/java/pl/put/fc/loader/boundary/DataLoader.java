package pl.put.fc.loader.boundary;

import com.fasterxml.jackson.databind.JsonNode;

public interface DataLoader {
    
    void loadEntitiesToDb(JsonNode node);
    
    void loadRelationsToDb(JsonNode node);
    
    void beginTransaction();
    
    void endTransaction();
}
