package pl.put.fc.client.boundary;

import java.util.List;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class AbstractApiRequester implements ApiRequester {
    
    protected int requestOnce(WebTarget target) {
        long startTime = System.currentTimeMillis();
        Response response = target.request().accept(MediaType.APPLICATION_JSON).get();
        List<Object> entities = response.readEntity(new GenericType<List<Object>>() {
        });
        entities.forEach(entity -> System.out.println(entity.toString()));
        System.out.println(entities.size());
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println(endTime);
        return (int) endTime;
    }
}
