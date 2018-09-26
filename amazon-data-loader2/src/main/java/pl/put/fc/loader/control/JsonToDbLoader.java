package pl.put.fc.loader.control;

import java.io.IOException;
import java.net.URL;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.put.fc.loader.boundary.DataLoader;

public class JsonToDbLoader {
    
    public void load(String fileName, DataLoader dataLoader) throws JsonProcessingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        URL fileURL = getClass().getClassLoader().getResource(fileName);
        long startTime = System.currentTimeMillis();
        loadEntities(dataLoader, objectMapper, fileURL);
        loadRelations(dataLoader, objectMapper, fileURL);
        System.out.println(dataLoader.getClass().getSimpleName() + ": " + (System.currentTimeMillis() - startTime));
    }
    
    private void loadEntities(DataLoader dataLoader, ObjectMapper objectMapper, URL fileURL)
            throws IOException, JsonParseException, JsonProcessingException {
        JsonParser parser = objectMapper.getFactory().createParser(fileURL);
        int i = 1;
        final int transactionSize = dataLoader.getNumberOfInsertsPerEntityTransaction();
        dataLoader.beginTransaction();
        while (parser.nextToken() != null) {
            JsonNode node = objectMapper.readTree(parser);
            dataLoader.loadEntitiesToDb(node);
            if ((i++ % transactionSize) == 0) {
                dataLoader.endTransaction();
                dataLoader.beginTransaction();
            }
        }
        dataLoader.endTransaction();
    }
    
    private void loadRelations(DataLoader dataLoader, ObjectMapper objectMapper, URL fileURL)
            throws IOException, JsonParseException, JsonProcessingException {
        JsonParser parser = objectMapper.getFactory().createParser(fileURL);
        int i = 1;
        final int transactionSize = dataLoader.getNumberOfInsertsPerRelationTransaction();
        dataLoader.beginTransaction();
        while (parser.nextToken() != null) {
            JsonNode node = objectMapper.readTree(parser);
            dataLoader.loadRelationsToDb(node);
            if ((i++ % transactionSize) == 0) {
                dataLoader.endTransaction();
                dataLoader.beginTransaction();
            }
        }
        dataLoader.endTransaction();
    }
}
