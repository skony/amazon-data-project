package pl.put.fc.api.orient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import pl.put.fc.model.orient.CategoryDefinition;
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
        Optional<OVertex> category = session.query("SELECT FROM Category WHERE name = ?", categoryName).vertexStream().findFirst();
        if (category.isPresent()) {
            List<OVertex> categoriesToSearchIn = getDescendantCategories(category.get());
            OResultSet query =
                    session.query("SELECT FROM ? UNWIND productCategory WHERE minPrice >= ? AND maxPrice <= ? AND ", categoriesToSearchIn,
                            minPrice, maxPrice);
            int x = 1;
            
        }
        return Collections.emptyList();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/review")
    public List<Review> getReviews(@QueryParam("user") String user, @QueryParam("product") String product) {
        if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(product)) {
            
        }
        if (StringUtils.isNotBlank(user)) {
            return session.query("SELECT FROM Review WHERE OUT('reviewReviewer').id = ?", user).vertexStream()
                    .map(Review::new)
                    .collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(product)) {
            return session.query("SELECT FROM Review WHERE OUT('reviewProduct').id = ?", product).vertexStream()
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
    
    private List<OVertex> getDescendantCategories(OVertex category) {
        List<OVertex> parentCategories = Arrays.asList(category);
        List<OVertex> descendantCategories = new ArrayList<>(Arrays.asList(category));
        while (true) {
            List<OVertex> childCategories = getChildCategories(parentCategories);
            if (childCategories.isEmpty()) {
                break;
            }
            descendantCategories.addAll(childCategories);
            parentCategories = childCategories;
        }
        return descendantCategories;
    }
    
    private List<OVertex> getChildCategories(List<OVertex> parentCategories) {
        List<OVertex> childCategories = new ArrayList<>();
        parentCategories.forEach(parentCategory -> childCategories.addAll(getChildCategories(parentCategory)));
        return childCategories;
    }
    
    private List<OVertex> getChildCategories(OVertex parentCategory) {
        String parentCategoryName = parentCategory.getProperty(CategoryDefinition.NAME);
        return session.query("SELECT FROM Category WHERE OUT('parentCategory') = ?", parentCategory.getIdentity().toString()).vertexStream()
                .collect(Collectors.toList());
    }
}
