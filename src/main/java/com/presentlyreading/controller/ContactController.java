package com.presentlyreading.controller;

import com.presentlyreading.model.ContactInquiry;
import com.presentlyreading.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * REST controller for fan contact form submissions.
 *
 * <p>Public:
 *   POST /api/contact — save an inquiry (fan form submission)
 *
 * <p>Admin (HTTP Basic required):
 *   GET  /api/contact/admin            — list all inquiries (newest first)
 *   GET  /api/contact/admin/unread     — count of unread messages
 *   PUT  /api/contact/admin/{id}/read  — mark a message as read
 *   POST /api/contact/admin/{id}/reply — send a reply email + record it
 */
@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    /** POST /api/contact — submit a contact message (public) */
    @PostMapping
    public ResponseEntity<Map<String, Object>> submitInquiry(@RequestBody ContactInquiry inquiry) {
        contactService.saveInquiry(inquiry);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Thanks for reaching out! We'll get back to you soon. \uD83C\uDF3F"
        ));
    }

    /** GET /api/contact/admin — all inquiries, newest first (admin only) */
    @GetMapping("/admin")
    public List<ContactInquiry> getAllInquiries() {
        return contactService.getAllInquiries();
    }

    /** GET /api/contact/admin/unread — number of unread messages (admin only) */
    @GetMapping("/admin/unread")
    public Map<String, Long> getUnreadCount() {
        return Map.of("count", contactService.countUnread());
    }

    /** PUT /api/contact/admin/{id}/read — mark message as read (admin only) */
    @PutMapping("/admin/{id}/read")
    public ResponseEntity<ContactInquiry> markRead(@PathVariable Long id) {
        return contactService.markRead(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/contact/admin/{id}/reply — send reply email (admin only) */
    @PostMapping("/admin/{id}/reply")
    public ResponseEntity<Map<String, Object>> sendReply(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String replyText = body.get("replyText");
        if (replyText == null || replyText.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Reply text is required"));
        }
        try {
            contactService.sendReply(id, replyText);
            return ResponseEntity.ok(Map.of("success", true, "message", "Reply sent!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
