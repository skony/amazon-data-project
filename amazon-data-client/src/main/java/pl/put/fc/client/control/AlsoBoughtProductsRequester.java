package pl.put.fc.client.control;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.ws.rs.client.WebTarget;
import pl.put.fc.client.boundary.AbstractApiRequester;
import pl.put.fc.client.entity.DatabasePath;

public class AlsoBoughtProductsRequester extends AbstractApiRequester {
    
    private static final List<String> REQUESTED_PRODUCT_IDS = Arrays.asList("1417030321", "B00004XQ83", "B0002E1G5C", "B0002CZW0Y",
            "B00029MTMQ", "B0002E2KPC", "B0002F7K7Y", "B0002F58TG", "B0002E2XCW", "B0001ARCFA", "B0002D0CEO", "0767851013", "B0002CZSJY",
            "B0002F7IIK", "B00006I5SB", "B0002CZVXM", "B0002E3CHC", "B0002CZSJO", "B0002DV7ZM", "B0002F7IN0", "B0002E1O2C", "B000068NW5",
            "B0002F52EW", "B0002D02IU", "B00006LVEU", "B0002D0E8S", "B00009W40D", "B0002CZUUG", "B0002D0B4K", "B0002E2S36");
    
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
        return null;
    }
    
    private int request(WebTarget target) {
        return (int) REQUESTED_PRODUCT_IDS.stream()
                .map(product -> {
                    WebTarget productTarget = target.path("/" + product + "/alsoBought");
                    return requestOnce(productTarget);
                }).mapToInt(i -> i)
                .average()
                .getAsDouble();
    }
}
