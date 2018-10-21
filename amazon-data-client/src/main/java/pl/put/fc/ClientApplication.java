package pl.put.fc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import pl.put.fc.client.boundary.ApiRequester;
import pl.put.fc.client.control.AlsoBoughtProductsRequester;
import pl.put.fc.client.control.ByCategoryAndPriceProductsRequester;
import pl.put.fc.client.control.ByNameProductsRequester;
import pl.put.fc.client.control.ProductReviewsRequester;
import pl.put.fc.client.entity.TimeResult;

public class ClientApplication {
    
    private static final String BASE_URI = "http://localhost:8081/myapp";
    private static final List<ApiRequester> API_REQUESTERS = Arrays.asList(new ProductReviewsRequester(), new ByNameProductsRequester(),
            new ByCategoryAndPriceProductsRequester(),
            new AlsoBoughtProductsRequester());
    
    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(BASE_URI);
        List<TimeResult> map = API_REQUESTERS.stream()
                .map(apiRequester -> {
                    Map<String, Integer> timeReslt = apiRequester.runRequesting(baseTarget);
                    return new TimeResult(timeReslt, apiRequester.getClass().getSimpleName());
                }).collect(Collectors.toList());
        map.forEach(timeResult -> {
            System.out.println(timeResult.getRequesterName());
            printTimeResults(timeResult.getTimeResults());
        });
    }
    
    private static void printTimeResults(Map<String, Integer> timeResults) {
        timeResults.entrySet().stream()
                .forEach(result -> System.out.println(result.getKey() + ": " + result.getValue()));
    }
}
