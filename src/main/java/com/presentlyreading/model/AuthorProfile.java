package com.presentlyreading.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Singleton author profile — always ID = 1.
 * Holds site-wide author metadata editable through the admin dashboard.
 */
@Entity
@Table(name = "author_profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Display name of the author / project. */
    @Column(nullable = false)
    private String name = "Presently Reading";

    /** Public website URL for the author (e.g. mikstermedia.com). */
    @Column(name = "website_url", length = 1024)
    private String websiteUrl;

    /** Contact / booking email address. */
    @Column(name = "contact_email", length = 512)
    private String contactEmail;

    /** Short tagline shown in headers and track cards. */
    @Column(length = 512)
    private String tagline;

    /** Spotify author/album profile URL. */
    @Column(name = "spotify_url", length = 1024)
    private String spotifyUrl;

    /** Instagram profile URL. */
    @Column(name = "instagram_url", length = 1024)
    private String instagramUrl;

    /** Facebook page URL. */
    @Column(name = "facebook_url", length = 1024)
    private String facebookUrl;
}
