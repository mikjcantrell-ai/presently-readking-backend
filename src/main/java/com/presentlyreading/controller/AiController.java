package com.presentlyreading.controller;

import com.presentlyreading.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/review")
    public ResponseEntity<Map<String, String>> generateReview(@RequestBody Map<String, String> request) {
        String title = request.getOrDefault("title", "Unknown Title");
        String author = request.getOrDefault("author", "Unknown Author");
        String genre = request.getOrDefault("genre", "General Fiction");
        String existingReview = request.get("existingReview");
        String action = request.getOrDefault("action", "scratch");

        String generatedHtml = aiService.generateReview(title, author, genre, existingReview, action);

        return ResponseEntity.ok(Map.of("generatedReview", generatedHtml));
    }
}
