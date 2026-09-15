import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.web.client.RestTemplate;
public class TestApi {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String encodedQuery = URLEncoder.encode("burn of the everflame", StandardCharsets.UTF_8).replace("+", "%20");
        String url = String.format("https://openlibrary.org/search.json?q=%s&limit=10", encodedQuery);
        String response = restTemplate.getForObject(url, String.class);
        System.out.println(response);
    }
}
