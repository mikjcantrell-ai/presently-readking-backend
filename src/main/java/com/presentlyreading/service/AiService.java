package com.presentlyreading.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public String generateReview(String title, String author, String genre, String existingReview) {
        if (geminiApiKey == null || geminiApiKey.trim().isEmpty()) {
            return "<p><em>AI integration is not configured. Please set the GEMINI_API_KEY environment variable.</em></p>";
        }

        String prompt;
        if (existingReview != null && !existingReview.trim().isEmpty()) {
            prompt = String.format(
                "Please polish, improve, and format the following draft book review for the book '%s' by %s (Genre: %s). " +
                "Make it sound professional, engaging, and written from a first-person perspective. " +
                "Format the review using basic HTML tags (like <p>, <h3>, <blockquote>) so it looks good on a blog. " +
                "Do not include markdown code block syntax (like ```html), just return the raw HTML string.\n\n" +
                "Draft Review to Polish:\n%s",
                title, author, genre, existingReview
            );
        } else {
            prompt = String.format(
                "Write a short, beautifully formatted book review for '%s' by %s. " +
                "The genre is %s. " +
                "The review should be 2-3 paragraphs long, written from a personal, engaging first-person perspective. " +
                "Format the review using basic HTML tags (like <p>, <h3>, <blockquote>) so it looks good on a blog. " +
                "Do not include markdown code block syntax (like ```html), just return the raw HTML string.",
                title, author, genre
            );
        }

        String url = GEMINI_API_URL + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construct the Gemini API request body payload
        Map<String, Object> requestBody = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if (!parts.isEmpty()) {
                        String text = (String) parts.get(0).get("text");
                        // Clean up markdown block if the model ignores the instruction
                        if (text.startsWith("```html\n")) {
                            text = text.substring(8);
                        } else if (text.startsWith("```html")) {
                            text = text.substring(7);
                        }
                        if (text.endsWith("```")) {
                            text = text.substring(0, text.length() - 3);
                        }
                        return text.trim();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "<p><em>Error generating review. Please check the backend logs.</em></p>";
        }

        return "<p><em>Failed to generate review.</em></p>";
    }
}
