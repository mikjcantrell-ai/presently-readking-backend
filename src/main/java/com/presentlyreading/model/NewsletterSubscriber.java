package com.presentlyreading.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * Stores fan email addresses from the newsletter signup form.
 *
 * <p>Email addresses are stored with a unique constraint so
 * duplicate signups are rejected gracefully.
 */
@Entity
@Table(
    name = "newsletter_subscribers",
    uniqueConstraints = @UniqueConstraint(name = "uq_subscriber_email", columnNames = "email")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "subscribed_at", nullable = false)
    private LocalDateTime subscribedAt = LocalDateTime.now();

    /** Allows soft-unsubscribe without deleting the record. */
    @Column(nullable = false)
    private boolean active = true;
}
