package com.presentlyreading.service;

import com.presentlyreading.model.AuthorProfile;
import com.presentlyreading.repository.AuthorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthorProfileService {

    private final AuthorProfileRepository repo;

    /** Returns the singleton author profile (always ID = 1). */
    public AuthorProfile getProfile() {
        return repo.findById(1L).orElseGet(() -> {
            // Auto-create on first access if the DataSeeder hasn't run yet
            AuthorProfile p = new AuthorProfile();
            p.setName("Presently Reading");
            return repo.save(p);
        });
    }

    /** Admin: update the profile fields that are present in the request body. */
    @Transactional
    public AuthorProfile updateProfile(AuthorProfile incoming) {
        AuthorProfile existing = getProfile();
        if (incoming.getName()        != null) existing.setName(incoming.getName());
        if (incoming.getWebsiteUrl()  != null) existing.setWebsiteUrl(incoming.getWebsiteUrl());
        if (incoming.getContactEmail()!= null) existing.setContactEmail(incoming.getContactEmail());
        if (incoming.getTagline()     != null) existing.setTagline(incoming.getTagline());
        if (incoming.getSpotifyUrl()  != null) existing.setSpotifyUrl(incoming.getSpotifyUrl());
        if (incoming.getInstagramUrl()!= null) existing.setInstagramUrl(incoming.getInstagramUrl());
        if (incoming.getFacebookUrl() != null) existing.setFacebookUrl(incoming.getFacebookUrl());
        return repo.save(existing);
    }
}
