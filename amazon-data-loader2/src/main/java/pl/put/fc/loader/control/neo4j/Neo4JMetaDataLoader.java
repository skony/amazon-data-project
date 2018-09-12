package pl.put.fc.loader.control.neo4j;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.neo4j.ogm.session.Session;
import com.fasterxml.jackson.databind.JsonNode;
import pl.put.fc.datamodel.CategoryStack;
import pl.put.fc.datamodel.MetaDataRow;
import pl.put.fc.loader.boundary.AbstractMetaDataLoader;
import pl.put.fc.model.neo4j.Category;
import pl.put.fc.model.neo4j.Product;

public class Neo4JMetaDataLoader extends AbstractMetaDataLoader {
    
    private Session session;
    
    public Neo4JMetaDataLoader(Session session) {
        this.session = session;
    }
    
    @Override
    public void loadEntitiesToDb(JsonNode node) {
        MetaDataRow dataRow = getRow(node);
        Product product = new Product();
        product.setId(dataRow.getProductId());
        product.setTitle(dataRow.getTitle());
        product.setPrice(dataRow.getPrice());
        product.setBrand(dataRow.getBrand());
        
        List<Category> categories = new ArrayList<>();
        List<CategoryStack> categoriesStacks = dataRow.getCategories();
        categoriesStacks.forEach(stack -> insertCategoryStack(categories, stack));
        product.setCategories(categories);
        
        session.save(product);
    }
    
    @Override
    public void loadRelationsToDb(JsonNode node) {
        MetaDataRow dataRow = getRow(node);
        Product product = session.load(Product.class, dataRow.getProductId());
        product.setAlsoViewed(getRelatedProducts(dataRow.getAlsoViewed()));
        product.setAlsoBought(getRelatedProducts(dataRow.getAlsoBought()));
        product.setBoughtTogether(getRelatedProducts(dataRow.getBoughtTogether()));
        product.setBuyAfterViewing(getRelatedProducts(dataRow.getBuyAfterViewing()));
        session.save(product);
    }
    
    @Override
    public void beginTransaction() {
        session.beginTransaction();
    }
    
    @Override
    public void endTransaction() {
        session.getTransaction().commit();
    }
    
    @Override
    public int getNumberOfInsertsPerEntityTransaction() {
        return 0;
    }
    
    @Override
    public int getNumberOfInsertsPerRelationTransaction() {
        return 0;
    }
    
    private void insertCategoryStack(List<Category> categories, CategoryStack stack) {
        ListIterator<String> listIterator = stack.getCategories().listIterator();
        Category lastCategory = null;
        while (listIterator.hasNext()) {
            lastCategory = insertCategory(stack, listIterator);
        }
        categories.add(lastCategory);
    }
    
    private Category insertCategory(CategoryStack stack, ListIterator<String> listIterator) {
        if (listIterator.hasPrevious()) {
            return insertChildCategory(stack, listIterator);
        }
        return insertRootCategory(listIterator);
    }
    
    private Category insertChildCategory(CategoryStack stack, ListIterator<String> listIterator) {
        int previousIndex = listIterator.previousIndex();
        String previous = stack.getCategories().get(previousIndex);
        Category parentCategory = session.load(Category.class, previous);
        Category category = new Category(listIterator.next(), parentCategory);
        session.save(category);
        return category;
    }
    
    private Category insertRootCategory(ListIterator<String> listIterator) {
        Category category = new Category(listIterator.next());
        session.save(category);
        return category;
    }
    
    private List<Product> getRelatedProducts(List<String> relatedProductsIds) {
        List<Product> relatedProducts = new ArrayList<>();
        relatedProductsIds.forEach(id -> getRelatedProduct(relatedProducts, id));
        return relatedProducts;
    }
    
    private void getRelatedProduct(List<Product> relatedProducts, String id) {
        Product relatedProduct = session.load(Product.class, id);
        if (relatedProduct != null) {
            relatedProducts.add(relatedProduct);
        }
    }
}
