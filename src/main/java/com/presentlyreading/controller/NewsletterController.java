package com.presentlyreading.controller;

import com.presentlyreading.service.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * REST controller for newsletter signups.
 *
 * <p>POST /api/newsletter/subscribe — accepts {"email": "fan@example.com"}.
 * Returns 200 OK if newly subscribed, 200 with alreadySubscribed=true if duplicate.
 */
@RestController
@RequestMapping("/api/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, Object>> subscribe(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email is required"));
        }
        boolean newSubscriber = newsletterService.subscribe(email);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "alreadySubscribed", !newSubscriber,
                "message", newSubscriber
                        ? "You're in! We'll let you know when new music drops. 🌿"
                        : "You're already on the list — we'll be in touch soon!"
        ));
    }
}
