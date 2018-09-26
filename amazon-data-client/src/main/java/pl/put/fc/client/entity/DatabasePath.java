package pl.put.fc.client.entity;

public enum DatabasePath {
    
    POSTGRES("/postgres"),
    MONGO("/mongo"),
    ORIENT("/orient"),
    NEO4J("/neo4j");
    
    private String path;
    
    private DatabasePath(String path) {
        this.path = path;
    }
    
    public String getPath() {
        return path;
    }
}
