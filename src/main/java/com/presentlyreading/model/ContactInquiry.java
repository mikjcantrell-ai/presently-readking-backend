package com.presentlyreading.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * Fan contact form submission — stored in SQLite with read/replied tracking.
 */
@Entity
@Table(name = "contact_inquiries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_name", nullable = false)
    private String senderName;

    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    @Column(nullable = false)
    private String subject;

    @Column(name = "message_body", nullable = false, columnDefinition = "TEXT")
    private String messageBody;

    @Column(name = "received_date", nullable = false)
    private LocalDateTime receivedDate = LocalDateTime.now();

    /** True once the admin has opened/read this message */
    @Column(name = "is_read", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean read = false;

    /** True once a reply has been sent via the admin dashboard */
    @Column(name = "is_replied", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean replied = false;

    /** The reply text that was sent, stored for reference */
    @Column(name = "reply_text", columnDefinition = "TEXT")
    private String replyText;

    /** When the reply was sent */
    @Column(name = "replied_date")
    private LocalDateTime repliedDate;
}
