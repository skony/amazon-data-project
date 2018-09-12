package pl.put.fc.loader.control.orient;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import com.fasterxml.jackson.databind.JsonNode;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import pl.put.fc.datamodel.CategoryStack;
import pl.put.fc.datamodel.MetaDataRow;
import pl.put.fc.loader.boundary.AbstractMetaDataLoader;
import pl.put.fc.model.orient.CategoryDefinition;
import pl.put.fc.model.orient.ProductDefinition;

public class OrientMetaDataLoader extends AbstractMetaDataLoader {
    
    private static final String QUERY_CATEGORY = "SELECT FROM Category WHERE name = ?";
    private static final String QUERY_PRODUCT = "SELECT FROM Product WHERE id = ?";
    
    private ODatabaseSession session;
    
    public OrientMetaDataLoader(ODatabaseSession session) {
        this.session = session;
    }
    
    @Override
    public void loadEntitiesToDb(JsonNode node) {
        MetaDataRow dataRow = getRow(node);
        OVertex product = session.newVertex(ProductDefinition.class.getSimpleName());
        
        product.setProperty(ProductDefinition.ID, dataRow.getProductId());
        product.setProperty(ProductDefinition.TITLE, dataRow.getTitle());
        product.setProperty(ProductDefinition.BRAND, dataRow.getBrand());
        product.setProperty(ProductDefinition.PRICE, dataRow.getPrice());
        
        List<OVertex> categories = new ArrayList<>();
        List<CategoryStack> categoriesStacks = dataRow.getCategories();
        categoriesStacks.forEach(stack -> insertCategoryStack(categories, stack));
        categories.forEach(category -> product.addEdge(category, ProductDefinition.CATEGORY));
        
        product.save();
    }
    
    @Override
    public void loadRelationsToDb(JsonNode node) {
        MetaDataRow dataRow = getRow(node);
        OVertex product = session.query(QUERY_PRODUCT, dataRow.getProductId()).next().getVertex().get();
        getRelatedProducts(dataRow.getAlsoViewed()).forEach(related -> product.addEdge(related, ProductDefinition.ALSO_VIEWED));
        getRelatedProducts(dataRow.getAlsoBought()).forEach(related -> product.addEdge(related, ProductDefinition.ALSO_BOUGHT));
        getRelatedProducts(dataRow.getBoughtTogether()).forEach(related -> product.addEdge(related, ProductDefinition.BOUGHT_TOGETHER));
        getRelatedProducts(dataRow.getBuyAfterViewing()).forEach(related -> product.addEdge(related, ProductDefinition.BUY_AFTER_VIEWING));
        session.save(product);
    }
    
    @Override
    public void beginTransaction() {
        session.begin();
    }
    
    @Override
    public void endTransaction() {
        session.commit();
    }
    
    private void insertCategoryStack(List<OVertex> categories, CategoryStack stack) {
        ListIterator<String> listIterator = stack.getCategories().listIterator();
        OVertex lastCategory = null;
        while (listIterator.hasNext()) {
            lastCategory = insertCategory(stack, listIterator);
        }
        categories.add(lastCategory);
    }
    
    private OVertex insertCategory(CategoryStack stack, ListIterator<String> listIterator) {
        if (listIterator.hasPrevious()) {
            return insertChildCategory(stack, listIterator);
        }
        return insertRootCategory(listIterator);
    }
    
    private OVertex insertChildCategory(CategoryStack stack, ListIterator<String> listIterator) {
        int previousIndex = listIterator.previousIndex();
        String previousCategoryName = stack.getCategories().get(previousIndex);
        OResultSet queryParentResult = session.query(QUERY_CATEGORY, previousCategoryName);
        OVertex parentCategory = queryParentResult.next().getVertex().get();
        String categoryName = listIterator.next();
        OResultSet queryResult = session.query(QUERY_CATEGORY, categoryName);
        if (queryResult.hasNext()) {
            return queryResult.next().getVertex().get();
        }
        OVertex category = session.newVertex(CategoryDefinition.class.getSimpleName());
        category.setProperty(CategoryDefinition.NAME, categoryName);
        category.addEdge(parentCategory, CategoryDefinition.PARENT_CATEGORY);
        category.save();
        return category;
    }
    
    private OVertex insertRootCategory(ListIterator<String> listIterator) {
        String categoryName = listIterator.next();
        OResultSet queryResult = session.query(QUERY_CATEGORY, categoryName);
        if (queryResult.hasNext()) {
            return queryResult.next().getVertex().get();
        }
        OVertex category = session.newVertex(CategoryDefinition.class.getSimpleName());
        category.setProperty(CategoryDefinition.NAME, categoryName);
        category.save();
        return category;
    }
    
    private List<OVertex> getRelatedProducts(List<String> relatedProductsIds) {
        List<OVertex> relatedProducts = new ArrayList<>();
        relatedProductsIds.forEach(id -> getRelatedProduct(relatedProducts, id));
        return relatedProducts;
    }
    
    private void getRelatedProduct(List<OVertex> relatedProducts, String id) {
        OResultSet queryResult = session.query(QUERY_PRODUCT, id);
        if (queryResult.hasNext()) {
            relatedProducts.add(queryResult.next().getVertex().get());
        }
    }
}
