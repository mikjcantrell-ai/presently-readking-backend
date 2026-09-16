package com.presentlyreading.service;

import com.presentlyreading.model.SiteContent;
import com.presentlyreading.repository.SiteContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteContentService {

    private final SiteContentRepository repo;

    /** Get all content entries as a list (for admin UI) */
    public List<SiteContent> getAll() {
        return repo.findAllByOrderBySectionAscKeyAsc();
    }

    /** Get all content as a flat key→value map (for public home page use) */
    public Map<String, String> getAllAsMap() {
        return repo.findAll().stream()
                .collect(Collectors.toMap(SiteContent::getKey, SiteContent::getValue));
    }

    /** Update a single content entry's value */
    public SiteContent update(String key, String newValue) {
        SiteContent item = repo.findById(key)
                .orElseThrow(() -> new RuntimeException("Content key not found: " + key));
        item.setValue(newValue);
        return repo.save(item);
    }

    /** Update multiple content entries in bulk */
    public void updateBulk(Map<String, String> updates) {
        updates.forEach((key, value) -> {
            repo.findById(key).ifPresent(item -> {
                item.setValue(value);
                repo.save(item);
            });
        });
    }

    /** Seed default content if not present (called by DataSeeder) */
    public void seedIfEmpty(List<SiteContent> defaults) {
        for (SiteContent item : defaults) {
            if (!repo.existsById(item.getKey())) {
                repo.save(item);
            }
        }
    }
}
