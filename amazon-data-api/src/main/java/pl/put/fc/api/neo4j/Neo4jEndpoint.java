package pl.put.fc.api.neo4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import pl.put.fc.model.neo4j.Category;
import pl.put.fc.model.neo4j.Product;
import pl.put.fc.model.neo4j.Review;

@Singleton
@Path("/neo4j")
public class Neo4jEndpoint {
    
    private Configuration configuration;
    private SessionFactory sessionFactory;
    private Session session;
    
    public Neo4jEndpoint() {
        configuration = new Configuration.Builder().uri("bolt://neo4j:root@localhost").build();
        sessionFactory = new SessionFactory(configuration, "pl.put.fc.model.neo4j");
        session = sessionFactory.openSession();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/{productId}/alsoBought")
    public List<Product> getProductsAlsoBought(@PathParam("productId") String productId) {
        Product product = session.load(Product.class, productId);
        if (product != null) {
            return product.getAlsoBought();
        }
        return Collections.emptyList();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product")
    public List<Product> getProducts(@QueryParam("query") String query) {
        Map<String, String> params = new HashMap<>();
        params.put("param", "(?i).*" + query + ".*");
        Iterable<Map<String, Object>> queryResults = session.query(
                "MATCH(product:Product)-[:PRODUCT_CATEGORY]->(category:Category) WHERE product.title =~ $param OR product.brand =~ $param RETURN product, category",
                params).queryResults();
        return Product.copyFromQuery(queryResults);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/category/{categoryName}")
    public List<Product> getProductsFromCategory(@DefaultValue("0.0") @QueryParam("minPrice") double minPrice,
            @DefaultValue("0x1.fffffffffffffP+1023") @QueryParam("maxPrice") double maxPrice,
            @PathParam("categoryName") String categoryName) {
        Category category = session.load(Category.class, categoryName);
        if (category != null) {
            List<String> categoriesToSearchIn = getDescendantCategories(category);
            Map<String, Object> params = new HashMap<>();
            params.put("minPrice", minPrice);
            params.put("maxPrice", maxPrice);
            params.put("categoryNames", categoriesToSearchIn);
            Iterable<Map<String, Object>> queryResults = session.query("MATCH(category:Category)<-[:PRODUCT_CATEGORY]-(product:Product) "
                    + "WHERE category.name IN $categoryNames AND product.price >= $minPrice AND product.price <= $maxPrice "
                    + "RETURN product, category",
                    params).queryResults();
            return Product.copyFromQuery(queryResults);
        }
        return Collections.emptyList();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/review")
    public List<Review> getReviews(@QueryParam("user") String user, @QueryParam("product") String product) {
        List<Review> result = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(product)) {
            
        }
        if (StringUtils.isNotBlank(user)) {
            params.put("user", user);
            session.query(
                    "MATCH(reviewer:Reviewer{uid:$user})<-[:REVIEW_REVIEWER]-(review:Review)-[:REVIEW_PRODUCT]->(product:Product) RETURN review, reviewer, product",
                    params)
                    .forEach(queryResult -> result.add(Review.copyFromQuery(queryResult)));
            
        }
        if (StringUtils.isNotBlank(product)) {
            params.put("product", product);
            session.query(
                    "MATCH(product:Product{uid:$product})<-[:REVIEW_PRODUCT]-(review:Review)-[:REVIEW_REVIEWER]->(reviewer:Reviewer) RETURN review, reviewer, product",
                    params)
                    .forEach(queryResult -> result.add(Review.copyFromQuery(queryResult)));
        }
        return result;
    }
    
    @Override
    protected void finalize() throws Throwable {
        session.clear();
        sessionFactory.close();
        super.finalize();
    }
    
    private List<String> getDescendantCategories(Category category) {
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
        return descendantCategories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
    
    private List<Category> getChildCategories(List<Category> parentCategories) {
        List<Category> childCategories = new ArrayList<>();
        parentCategories.forEach(parentCategory -> childCategories.addAll(getChildCategories(parentCategory.getName())));
        return childCategories;
    }
    
    private List<Category> getChildCategories(String categoryName) {
        List<Category> result = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("categoryName", categoryName);
        session.query(Category.class, "MATCH(:Category{name:$categoryName})<-[:PARENT_CATEGORY]-(child:Category) return child", params)
                .forEach(result::add);
        return result;
    }
}
