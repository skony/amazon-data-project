package pl.put.fc.client.boundary;

import java.util.Map;
import javax.ws.rs.client.WebTarget;

public interface ApiRequester {
    
    Map<String, Integer> runRequesting(WebTarget baseTarget);
}
