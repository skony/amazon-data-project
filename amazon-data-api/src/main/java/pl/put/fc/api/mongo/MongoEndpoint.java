package pl.put.fc.api.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.inject.Singleton;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import com.mongodb.MongoClient;
import pl.put.fc.model.mongo.Category;
import pl.put.fc.model.mongo.Product;
import pl.put.fc.model.mongo.Review;
import pl.put.fc.model.mongo.Reviewer;

@Singleton
@Path("/mongo")
public class MongoEndpoint {
    
    private Morphia morphia;
    private Datastore datastore;
    
    public MongoEndpoint() {
        morphia = new Morphia();
        morphia.mapPackage("pl.put.fc.model.mongo");
        datastore = morphia.createDatastore(new MongoClient(), "amazondb");
        datastore.ensureIndexes();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product")
    public List<Product> getProducts(@QueryParam("query") String query) {
        Pattern regexp = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        Query<Product> productQuery = datastore.createQuery(Product.class);
        productQuery.or(productQuery.criteria("title").equal(regexp), productQuery.criteria("brand").equal(regexp));
        return productQuery.asList();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/category/{categoryName}")
    public List<Product> getProductsFromCategory(@DefaultValue("0.0") @QueryParam("minPrice") double minPrice,
            @DefaultValue("0x1.fffffffffffffP+1023") @QueryParam("maxPrice") double maxPrice,
            @PathParam("categoryName") String categoryName) {
        Category category = datastore.get(Category.class, categoryName);
        if (category != null) {
            List<Category> categoriesToSearchIn = getDescendantCategories(category);
            Query<Product> query = datastore.createQuery(Product.class);
            query.and(query.criteria("price").greaterThanOrEq(minPrice), query.criteria("price").lessThanOrEq(maxPrice),
                    query.criteria("categories").hasAnyOf(categoriesToSearchIn));
            return query.asList();
        }
        return Collections.emptyList();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/review")
    public List<Review> getReviews(@QueryParam("user") String user, @QueryParam("product") String product) {
        if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(product)) {
            Query<Review> query = datastore.createQuery(Review.class);
            query.and(query.criteria("reviewer.id").equal(user), query.criteria("product.id").equal(product));
        }
        if (StringUtils.isNotBlank(user)) {
            Reviewer userObj = datastore.get(Reviewer.class, user);
            return datastore.createQuery(Review.class).field("reviewer").equal(userObj).asList();
        }
        if (StringUtils.isNotBlank(product)) {
            Product productObj = datastore.get(Product.class, product);
            return datastore.createQuery(Review.class).field("product").equal(productObj).asList();
        }
        return Collections.emptyList();
    }
    
    private List<Category> getDescendantCategories(Category category) {
        List<Category> parentCategories = Arrays.asList(category);
        List<Category> descendantCategories = new ArrayList<>(Arrays.asList(category));
        while (true) {
            List<Category> childCategories = getChildCategories(parentCategories);
            if (childCategories.isEmpty()) {
                break;
            }
            descendantCategories.addAll(childCategories);
            parentCategories = childCategories;
        }
        return descendantCategories;
    }
    
    private List<Category> getChildCategories(List<Category> parentCategories) {
        List<Category> childCategories = new ArrayList<>();
        parentCategories.forEach(parentCategory -> childCategories.addAll(getChildCategories(parentCategory)));
        return childCategories;
    }
    
    private List<Category> getChildCategories(Category parentCategory) {
        return datastore.createQuery(Category.class).field("parentCategory").equal(parentCategory).asList();
    }
}
