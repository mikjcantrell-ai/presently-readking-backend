package com.presentlyreading.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * JPA entity representing a Presently Reading book / track.
 *
 * <p>Hibernate will auto-create the {@code books} table in {@code presentlyreading.db}
 * on first startup (ddl-auto=update).
 */
@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Display title of the book. */
    @Column(nullable = false)
    private String title;

    /**
     * Purchase URL (e.g. Amazon, local bookstore).
     */
    @Column(name = "purchase_url", length = 1024)
    private String purchaseUrl;

    /**
     * Goodreads or StoryGraph URL.
     */
    @Column(name = "goodreads_url", length = 1024)
    private String goodreadsUrl;

    /**
     * URL to the cover art / album artwork image.
     * If null, Angular falls back to the default album_art asset.
     */
    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    /** Primary genre tag. e.g. "Fantasy Romance" */
    @Column
    private String genre;

    /** Four-digit release year. */
    @Column(name = "release_year")
    private Integer releaseYear;

    /** Author name of the book. */
    @Column(name = "author_name", length = 512)
    private String authorName;

    /**
     * Whether this track is featured on the home page hero / music section.
     * Defaults to true for seeded tracks.
     */
    @Column(name = "featured_status", nullable = false)
    private boolean featuredStatus = true;

    /**
     * Reading status: "READ", "CURRENTLY_READING", "TBR"
     */
    @Column(name = "reading_status", length = 32)
    private String readingStatus = "READ";

    /**
     * Admin-controlled display order. Lower = shown first.
     * Defaults to 0 (insertion order).
     */
    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    /** Short teaser / tagline shown on the track card. */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** Full book review (HTML or Markdown). */
    @Column(name = "full_review", columnDefinition = "TEXT")
    private String fullReview;
}
