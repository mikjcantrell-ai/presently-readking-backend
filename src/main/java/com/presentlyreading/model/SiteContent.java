package com.presentlyreading.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Editable site content — key/value pairs grouped by page section.
 * Admin can update values; the frontend loads them dynamically.
 */
@Entity
@Table(name = "site_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SiteContent {

    @Id
    @Column(name = "content_key", length = 80)
    private String key;

    /** Human-readable label shown in the admin UI */
    @Column(nullable = false, length = 120)
    private String label;

    /** Grouping shown in admin UI (e.g. "Hero", "About", "Newsletter") */
    @Column(nullable = false, length = 60)
    private String section;

    /** The actual editable text */
    @Column(name = "content_value", nullable = false, columnDefinition = "TEXT")
    private String value;
}
