package com.presentlyreading.service;

import com.presentlyreading.model.BookImportDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleBooksService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<BookImportDto> searchBooks(String query) {
        try {
            // Switching to Open Library API to avoid Google Books rate limits
            String url = "https://openlibrary.org/search.json?q={query}&limit=10";
            
            System.out.println("Calling Open Library API: " + url + " with query: " + query);
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, query);
            JsonNode root = mapper.readTree(response.getBody());
            
            List<BookImportDto> books = new ArrayList<>();
            JsonNode docs = root.path("docs");
            
            if (docs.isArray()) {
                for (JsonNode doc : docs) {
                    books.add(parseBook(doc));
                }
            }
            return books;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse Open Library response", e);
        }
    }

    private BookImportDto parseBook(JsonNode doc) {
        BookImportDto dto = new BookImportDto();
        dto.setId(doc.path("key").asText());
        
        dto.setTitle(doc.path("title").asText());
        
        JsonNode authorName = doc.path("author_name");
        if (authorName.isArray() && authorName.size() > 0) {
            dto.setAuthorName(authorName.get(0).asText());
        }
        
        if (doc.has("first_publish_year")) {
            dto.setReleaseYear(doc.path("first_publish_year").asInt());
        }
        
        if (doc.has("cover_i")) {
            String coverId = doc.path("cover_i").asText();
            dto.setImageUrl("https://covers.openlibrary.org/b/id/" + coverId + "-L.jpg");
        }
        
        // Open library doesn't easily return purchase/info link per book in the search,
        // but we can provide the open library link as a fallback.
        if (doc.has("key")) {
            dto.setPurchaseUrl("https://openlibrary.org" + doc.path("key").asText());
        }
        
        return dto;
    }
}
