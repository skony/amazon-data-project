package pl.put.fc.endpoint.postgres;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/postgres/category")
public class CategoryPostgresEndpoint {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{categoryName}/note")
    public void getWithBestNotes(@QueryParam("limit") int limit) {
        
    }
}
