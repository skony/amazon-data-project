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
    
    private static final List<String> REQUESTED_PRODUCT_IDS = Arrays.asList("B0002E3L1E", "B0002F6HY6", "B0002E2OSK", "B0002E38WG",
            "B0002E3L00", "B0002E3L2I", "B0002F779K", "B0002E2QCY", "B0002F77PE", "B0002E3L0K", "B0002E2OTE", "B0002E2UCA", "B0002E38WQ",
            "B0002E3KY2", "B0002CZW8G", "B0002E38W6", "B0002F778G", "B0002E3L0U", "B0002E54FK", "B0002F778Q", "B0002CZWKO", "B0002F7790",
            "B0002E2OV2", "B0002F77AO", "B0002F742A", "B0002F7JTI", "B0002E38X0", "B0002E2OSU", "B0002E2QUQ", "B0002F73ZI");
    
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
        return (int) REQUESTED_PRODUCT_IDS.stream()
                .map(product -> {
                    WebTarget productTarget = target.path("/" + product + "/alsoBought");
                    return requestOnce(productTarget);
                }).mapToInt(i -> i)
                .average()
                .getAsDouble();
    }
}
