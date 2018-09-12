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
        long startTime = System.currentTimeMillis();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        URL fileURL = getClass().getClassLoader().getResource(fileName);
        loadEntities(dataLoader, objectMapper, fileURL);
        loadRelations(dataLoader, objectMapper, fileURL);
        System.out.println(System.currentTimeMillis() - startTime);
    }
    
    private void loadEntities(DataLoader dataLoader, ObjectMapper objectMapper, URL fileURL)
            throws IOException, JsonParseException, JsonProcessingException {
        JsonParser parser = objectMapper.getFactory().createParser(fileURL);
        int i = 1;
        dataLoader.beginTransaction();
        while (parser.nextToken() != null) {
            System.out.println("e " + i);
            JsonNode node = objectMapper.readTree(parser);
            // if ((i++ % 100) != 0) {
            // continue;
            // }
            dataLoader.loadEntitiesToDb(node);
            if ((i++ % 100) == 0) {
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
        dataLoader.beginTransaction();
        while (parser.nextToken() != null) {
            System.out.println("r " + i);
            JsonNode node = objectMapper.readTree(parser);
            // if ((i++ % 100) != 0) {
            // continue;
            // }
            dataLoader.loadRelationsToDb(node);
            if ((i++ % 50) == 0) {
                dataLoader.endTransaction();
                dataLoader.beginTransaction();
            }
        }
        dataLoader.endTransaction();
    }
}
