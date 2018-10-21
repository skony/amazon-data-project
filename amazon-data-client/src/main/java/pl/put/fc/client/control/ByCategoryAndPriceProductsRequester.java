package pl.put.fc.client.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.ws.rs.client.WebTarget;
import pl.put.fc.client.boundary.AbstractApiRequester;
import pl.put.fc.client.entity.DatabasePath;

public class ByCategoryAndPriceProductsRequester extends AbstractApiRequester {
    
    private static final List<String> QUERIED_CATEGORIES =
            Arrays.asList("Instrument Accessories", "Studio Recording Equipment", "Wind & Woodwind Instruments",
                    "Microphones & Accessories",
                    "Amplifiers & Effects", "DJ, Electronic Music & Karaoke", "Band & Orchestra", "Drums & Percussion",
                    "Live Sound & Stage", "Keyboards",
                    "Bass Guitars",
                    "Guitars",
                    "Stringed Instruments",
                    "Movies", "TV", "Special Interest", "Jazz", "Rock", "Pop", "Alternative Rock", "World Music", "Children's Music",
                    "Classical", "New Age", "Dance & Electronic", "Blues", "Classic Rock", "Country", "Folk", "Reggae");
    
    public Map<String, Integer> runRequesting(WebTarget baseTarget) {
        Map<String, Integer> map = new HashMap<>();
        Stream.of(DatabasePath.values())
                .forEach(dbPath -> {
                    WebTarget reviewTarget = baseTarget.path(dbPath.getPath() + "/product/category");
                    int avgTime = request(reviewTarget);
                    map.put(dbPath.getPath(), avgTime);
                });
        map.entrySet().stream().forEach(entry -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });
        return map;
    }
    
    private int request(WebTarget target) {
        return (int) QUERIED_CATEGORIES.stream()
                .map(category -> {
                    WebTarget productTarget = target.path("/" + category).queryParam("minPrice", "1.0").queryParam("maxPrice", "100.0");
                    return requestOnce(productTarget);
                }).mapToInt(i -> i)
                .average()
                .getAsDouble();
    }
}
