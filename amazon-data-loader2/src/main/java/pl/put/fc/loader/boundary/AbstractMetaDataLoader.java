package pl.put.fc.loader.boundary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import pl.put.fc.datamodel.CategoryStack;
import pl.put.fc.datamodel.MetaDataRow;

public abstract class AbstractMetaDataLoader implements DataLoader {
    
    private static final String PRODUCT_ID = "asin";
    private static final String TITLE = "title";
    private static final String PRICE = "price";
    private static final String IMG_URL = "imUrl";
    private static final String RELATED = "related";
    private static final String ALSO_BOUGHT = "also_bought";
    private static final String ALSO_VIEWED = "also_viewed";
    private static final String BOUGHT_TOGETHER = "bought_together";
    private static final String BUY_AFTER_VIEWING = "buy_after_viewing";
    private static final String BRAND = "brand";
    private static final String SALES_RANK = "salesRank";
    private static final String CATEGORIES = "categories";
    
    protected MetaDataRow getRow(JsonNode node) {
        MetaDataRow metaDataRow = new MetaDataRow();
        metaDataRow.setProductId(node.path(PRODUCT_ID).asText());
        metaDataRow.setTitle(node.path(TITLE).asText());
        metaDataRow.setPrice(node.path(PRICE).asDouble());
        metaDataRow.setImgUrl(node.path(IMG_URL).asText());
        metaDataRow.setBrand(node.path(BRAND).asText());
        
        metaDataRow.setCategories(getCategories(node));
        
        metaDataRow.setAlsoBought(getRelatedList(node, ALSO_BOUGHT));
        metaDataRow.setAlsoViewed(getRelatedList(node, ALSO_VIEWED));
        metaDataRow.setBoughtTogether(getRelatedList(node, BOUGHT_TOGETHER));
        metaDataRow.setBuyAfterViewing(getRelatedList(node, BUY_AFTER_VIEWING));
        
        return metaDataRow;
    }
    
    private List<String> getRelatedList(JsonNode node, String jsonArrayName) {
        Iterator<JsonNode> jsonArrayIterator = node.path(RELATED).path(jsonArrayName).elements();
        List<String> jsonArrayElements = new ArrayList();
        while (jsonArrayIterator.hasNext()) {
            jsonArrayElements.add(jsonArrayIterator.next().asText());
        }
        return jsonArrayElements;
    }
    
    private List<CategoryStack> getCategories(JsonNode node) {
        Iterator<JsonNode> categoriesStackIterator = node.path(CATEGORIES).elements();
        List<CategoryStack> categories = new ArrayList();
        while (categoriesStackIterator.hasNext()) {
            Iterator<JsonNode> categoriesIterator = categoriesStackIterator.next().elements();
            categories.add(getCategoryStack(categoriesIterator));
        }
        return categories;
    }
    
    private CategoryStack getCategoryStack(Iterator<JsonNode> categoriesIterator) {
        LinkedList<String> categoryStackList = new LinkedList<>();
        while (categoriesIterator.hasNext()) {
            categoryStackList.add(categoriesIterator.next().asText());
        }
        return new CategoryStack(categoryStackList);
    }
}
