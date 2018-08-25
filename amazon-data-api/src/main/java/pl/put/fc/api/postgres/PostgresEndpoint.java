package pl.put.fc.api.postgres;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pl.put.fc.model.postgres.Category;
import pl.put.fc.model.postgres.Product;
import pl.put.fc.model.postgres.Review;

@Singleton
@Path("/postgres")
public class PostgresEndpoint {
    
    private SessionFactory sessionFactory;
    private Session session;
    
    public PostgresEndpoint() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        session = sessionFactory.openSession();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product")
    public List<Product> getProducts(@QueryParam("query") String query) {
        return session
                .createQuery("FROM Product p WHERE lower(p.title) like :title OR lower(p.brand) like :brand", Product.class)
                .setParameter("title", '%' + query.toLowerCase() + '%')
                .setParameter("brand", '%' + query.toLowerCase() + '%')
                .getResultList();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/product/category/{categoryName}")
    public List<Product> getProductsFromCategory(@DefaultValue("0.0") @QueryParam("minPrice") double minPrice,
            @DefaultValue("0x1.fffffffffffffP+1023") @QueryParam("maxPrice") double maxPrice,
            @PathParam("categoryName") String categoryName) {
        Category category = session.get(Category.class, categoryName);
        if (category != null) {
            List<String> categoriesToSearchIn = getDescendantCategories(category);
            return session.createQuery(
                    "SELECT p FROM Product p JOIN p.categories c WHERE c.name IN (:categories) AND p.price >= :minPrice AND p.price <= :maxPrice",
                    Product.class)
                    .setParameter("minPrice", minPrice)
                    .setParameter("maxPrice", maxPrice)
                    .setParameterList("categories", categoriesToSearchIn)
                    .setMaxResults(3)
                    .getResultList();
        }
        return Collections.emptyList();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/review")
    public List<Review> getReviews(@QueryParam("user") String user, @QueryParam("product") String product) {
        if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(product)) {
            return session.createQuery("FROM Review r WHERE r.reviewer.id = :user AND r.product.id = :product")
                    .setParameter("user", user)
                    .setParameter("product", product)
                    .getResultList();
        }
        if (StringUtils.isNotBlank(user)) {
            return session.createQuery("FROM Review r WHERE r.reviewer.id = :user")
                    .setParameter("user", user)
                    .getResultList();
        }
        if (StringUtils.isNotBlank(product)) {
            return session.createQuery("FROM Review r where r.product.id = :product")
                    .setMaxResults(10)
                    .setParameter("product", product)
                    .getResultList();
        }
        return Collections.emptyList();
    }
    
    @Override
    protected void finalize() throws Throwable {
        session.close();
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
        parentCategories.forEach(parentCategory -> childCategories.addAll(getChildCategories(parentCategory)));
        return childCategories;
    }
    
    private List<Category> getChildCategories(Category parentCategory) {
        return session
                .createQuery("FROM Category c WHERE c.parentCategory.name = :parentCategory", Category.class)
                .setParameter("parentCategory", parentCategory.getName())
                .getResultList();
    }
}
