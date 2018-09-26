package pl.put.fc.client.entity;

import java.util.Map;

public class TimeResult {
    
    private Map<String, Integer> timeResults;
    private String requesterName;
    
    public TimeResult(Map<String, Integer> timeResults, String requesterName) {
        this.timeResults = timeResults;
        this.requesterName = requesterName;
    }
    
    public Map<String, Integer> getTimeResults() {
        return timeResults;
    }
    
    public String getRequesterName() {
        return requesterName;
    }
}
