package pl.put.fc.loader.control.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.mongodb.morphia.Datastore;
import com.fasterxml.jackson.databind.JsonNode;
import pl.put.fc.datamodel.CategoryStack;
import pl.put.fc.datamodel.MetaDataRow;
import pl.put.fc.loader.boundary.AbstractMetaDataLoader;
import pl.put.fc.model.mongo.Category;
import pl.put.fc.model.mongo.Product;

public class MongoMetaDataLoader extends AbstractMetaDataLoader {
    
    private Datastore datastore;
    
    public MongoMetaDataLoader(Datastore datastore) {
        this.datastore = datastore;
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
        product.setCategory(categories);
        
        datastore.save(product);
    }
    
    @Override
    public void loadRelationsToDb(JsonNode node) {
        MetaDataRow dataRow = getRow(node);
        Product product = datastore.get(Product.class, dataRow.getProductId());
        product.setAlsoViewed(getRelatedProducts(dataRow.getAlsoViewed()));
        product.setAlsoBought(getRelatedProducts(dataRow.getAlsoBought()));
        product.setBoughtTogether(getRelatedProducts(dataRow.getBoughtTogether()));
        product.setBuyAfterViewing(getRelatedProducts(dataRow.getBuyAfterViewing()));
        datastore.save(product);
    }
    
    @Override
    public void beginTransaction() {
    }
    
    @Override
    public void endTransaction() {
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
        Category parentCategory = datastore.get(Category.class, previous);
        Category category = new Category(listIterator.next(), parentCategory);
        datastore.save(category);
        return category;
    }
    
    private Category insertRootCategory(ListIterator<String> listIterator) {
        Category category = new Category(listIterator.next());
        datastore.save(category);
        return category;
    }
    
    private List<Product> getRelatedProducts(List<String> relatedProductsIds) {
        List<Product> relatedProducts = new ArrayList<>();
        relatedProductsIds.forEach(id -> getRelatedProduct(relatedProducts, id));
        return relatedProducts;
    }
    
    private void getRelatedProduct(List<Product> relatedProducts, String id) {
        Product relatedProduct = datastore.get(Product.class, id);
        if (relatedProduct != null) {
            relatedProducts.add(relatedProduct);
        }
    }
}
