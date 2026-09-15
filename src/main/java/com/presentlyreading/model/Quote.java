package com.presentlyreading.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * One quote block (verse, chorus, bridge, etc.) belonging to a {@link Book}.
 *
 * <p>The quotes page renders blocks in {@code displayOrder} sequence, using
 * {@code sectionType} to apply different visual styles (chorus = gold accent,
 * bridge = sage accent, outro = italic, etc.).
 */
@Entity
@Table(name = "quotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The book this quote block belongs to. */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /**
     * Human-readable section label displayed above the block.
     * e.g. "Verse 1", "Pre-Chorus", "Chorus", "Bridge", "Final Chorus", "Outro"
     */
    @Column(name = "section_label", nullable = false)
    private String sectionLabel;

    /**
     * Section type drives the visual style applied in Angular.
     * Values: VERSE | PRE_CHORUS | CHORUS | BRIDGE | OUTRO
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "section_type", nullable = false)
    private SectionType sectionType;

    /**
     * The actual quote text. Newlines within the block are preserved
     * and rendered as {@code <br>} in the Angular template.
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Controls the order blocks appear on the quotes page.
     * Blocks are fetched ORDER BY displayOrder ASC.
     */
    @Column(name = "display_order", nullable = false)
    private int displayOrder;

    /** Quote section type enum. */
    public enum SectionType {
        VERSE, PRE_CHORUS, CHORUS, BRIDGE, OUTRO
    }
}
