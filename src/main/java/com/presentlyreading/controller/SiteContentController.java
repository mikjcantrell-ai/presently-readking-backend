package com.presentlyreading.controller;

import com.presentlyreading.model.SiteContent;
import com.presentlyreading.service.SiteContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * REST controller for editable site content (hero text, about blurbs, etc.)
 *
 * <p>Public:
 *   GET /api/content/map — returns all content as {key: value} (used by home page)
 *
 * <p>Admin (HTTP Basic required):
 *   GET /api/content         — returns full list with labels/sections (admin UI)
 *   PUT /api/content/{key}   — update a single content value
 */
@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class SiteContentController {

    private final SiteContentService service;

    /** GET /api/content/map — flat key→value map for the public home page */
    @GetMapping("/map")
    public Map<String, String> getMap() {
        return service.getAllAsMap();
    }

    /** GET /api/content — full list for admin UI (section, label, key, value) */
    @GetMapping
    public List<SiteContent> getAll() {
        return service.getAll();
    }

    /** PUT /api/content/{key} — update value (admin only) */
    @PutMapping("/{key}")
    public ResponseEntity<SiteContent> update(
            @PathVariable String key,
            @RequestBody Map<String, String> body) {
        String newValue = body.get("value");
        if (newValue == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(service.update(key, newValue));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
