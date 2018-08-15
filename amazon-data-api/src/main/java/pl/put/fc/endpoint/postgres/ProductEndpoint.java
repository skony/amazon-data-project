package pl.put.fc.endpoint.postgres;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import pl.put.fc.model.postgres.Product;

@Path("/product")
public class ProductEndpoint {
    
    private String name;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Product getTest() {
        Product product = new Product();
        product.setTitle(name);
        return product;
    }
    
    public ProductEndpoint() {
        name = "HHHHH";
    }
}
