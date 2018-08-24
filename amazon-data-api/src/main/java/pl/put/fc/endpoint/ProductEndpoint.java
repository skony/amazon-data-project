package pl.put.fc.endpoint;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import pl.put.fc.model.postgres.Product;

public interface ProductEndpoint {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Product> getProducts(@QueryParam("query") String query);
}
