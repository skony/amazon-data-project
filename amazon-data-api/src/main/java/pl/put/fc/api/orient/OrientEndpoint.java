package pl.put.fc.api.orient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import pl.put.fc.model.orient.Product;
import pl.put.fc.model.orient.Review;

@Singleton
@Path("/orient")
public class OrientEndpoint {
    
    private OrientDB orientDb;
    private ODatabaseSession session;
    
    public OrientEndpoint() {
        orientDb = new OrientDB("remote:localhost/amazondb", "root", "root", OrientDBConfig.defaultConfig());
        session = orientDb.open("amazondb", "root", "root");
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product")
    public List<Product> getProducts(@QueryParam("query") String query) {
        String param = '%' + query.toLowerCase() + '%';
        return session.query("SELECT FROM Product WHERE title.toLowerCase() like ? OR brand.toLowerCase() like ?", param, param)
                .vertexStream()
                .map(Product::new)
                .collect(Collectors.toList());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/category/{categoryName}")
    public List<Product> getProductsFromCategory(@DefaultValue("0.0") @QueryParam("minPrice") double minPrice,
            @DefaultValue("0x1.fffffffffffffP+1023") @QueryParam("maxPrice") double maxPrice,
            @PathParam("categoryName") String categoryName) {
        return session.query("SELECT expand(product) FROM (MATCH {class: Category, as:category, where: (name = ?)}" +
                ".in('parentCategory'){as: child, while: (in('parentCategory').size() > 0)}" +
                ".in('productCategory'){as:product, where: (price >= ? AND price <= ?)}" +
                "return product)", categoryName, minPrice, maxPrice)
                .vertexStream()
                .map(Product::new)
                .collect(Collectors.toList());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/review")
    public List<Review> getReviews(@QueryParam("user") String user, @QueryParam("product") String product) {
        if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(product)) {
            
        }
        if (StringUtils.isNotBlank(user)) {
            return session.query("SELECT expand(review) FROM (" +
                    "MATCH {class: Reviewer, as: reviewer, where: (id = ?)}" +
                    ".in('reviewReviewer'){as: review} return review)", user).vertexStream()
                    .map(Review::new)
                    .collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(product)) {
            return session.query("SELECT expand(review) FROM (" +
                    "MATCH {class: Product, as: product, where: (id = ?)}" +
                    ".in('reviewProduct'){as: review} return review)", product).vertexStream()
                    .map(Review::new)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    @Override
    protected void finalize() throws Throwable {
        session.close();
        orientDb.close();
        super.finalize();
    }
}
