package pl.put.fc.client.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.ws.rs.client.WebTarget;
import pl.put.fc.client.boundary.AbstractApiRequester;
import pl.put.fc.client.entity.DatabasePath;

public class ByNameProductsRequester extends AbstractApiRequester {
    
    private static final List<String> QUERIED_NAMES = Arrays.asList("clapton", "kjos", "alfred", "japanese",
            "gospel", "blues", "organ", "arias", "folk", "bach", "piano", "beethoven", "haydn",
            "yamaha", "panasonic", "pioneer", "freeman", "jordan", "schubert", "latin", "minogue", "goldberg",
            "karaoke", "disney", "dj", "rock", "symphony", "mozart", "sony", "casio");
    
    public Map<String, Integer> runRequesting(WebTarget baseTarget) {
        Map<String, Integer> map = new HashMap<>();
        Stream.of(DatabasePath.values())
                .forEach(dbPath -> {
                    WebTarget reviewTarget = baseTarget.path(dbPath.getPath() + "/product");
                    int avgTime = request(reviewTarget);
                    map.put(dbPath.getPath(), avgTime);
                });
        map.entrySet().stream().forEach(entry -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });
        return map;
    }
    
    private int request(WebTarget target) {
        return (int) QUERIED_NAMES.stream()
                .map(product -> {
                    WebTarget productTarget = target.queryParam("query", product);
                    return requestOnce(productTarget);
                }).mapToInt(i -> i)
                .average()
                .getAsDouble();
    }
}
