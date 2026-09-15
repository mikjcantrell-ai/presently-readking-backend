package com.presentlyreading.controller;

import com.presentlyreading.model.AuthorProfile;
import com.presentlyreading.service.AuthorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for the singleton author profile.
 *
 * <p>GET /api/author  — public (used by About page, Music page)
 * <p>PUT /api/author  — admin (HTTP Basic required)
 */
@RestController
@RequestMapping("/api/author")
@RequiredArgsConstructor
public class AuthorProfileController {

    private final AuthorProfileService service;

    /** GET /api/author — fetch the public author profile */
    @GetMapping
    public AuthorProfile getProfile() {
        return service.getProfile();
    }

    /** PUT /api/author — admin: update any author profile fields */
    @PutMapping
    public AuthorProfile updateProfile(@RequestBody AuthorProfile profile) {
        return service.updateProfile(profile);
    }
}
