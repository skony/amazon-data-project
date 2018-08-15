package pl.put.fc.endpoint.postgres;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/category")
public class CategoryEndpoint {
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTest() {
        return "CATEGORY";
    }
}
