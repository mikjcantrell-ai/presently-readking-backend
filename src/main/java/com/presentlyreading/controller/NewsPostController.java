package com.presentlyreading.controller;

import com.presentlyreading.model.NewsPost;
import com.presentlyreading.service.NewsPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsPostController {

    private final NewsPostService service;

    public NewsPostController(NewsPostService service) {
        this.service = service;
    }

    /** GET /api/news — public endpoint, returns only published news */
    @GetMapping
    public List<NewsPost> getPublicNews() {
        return service.getPublicNews();
    }

    /** GET /api/news/admin — admin endpoint, returns all news */
    @GetMapping("/admin")
    public List<NewsPost> getAllNewsForAdmin() {
        return service.getAllNewsForAdmin();
    }

    /** POST /api/news — create a news post */
    @PostMapping
    public NewsPost createNews(@RequestBody NewsPost post) {
        return service.createNews(post);
    }

    /** PUT /api/news/{id} — update a news post */
    @PutMapping("/{id}")
    public ResponseEntity<NewsPost> updateNews(@PathVariable Long id, @RequestBody NewsPost post) {
        return service.updateNews(id, post)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/news/{id} — delete a news post */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        if (service.deleteNews(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
