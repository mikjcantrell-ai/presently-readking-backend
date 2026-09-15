package com.presentlyreading.service;

import com.presentlyreading.model.ContactInquiry;
import com.presentlyreading.repository.ContactInquiryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final ContactInquiryRepository inquiryRepository;
    private final JavaMailSender mailSender;

    /** Save an incoming contact form submission */
    public ContactInquiry saveInquiry(ContactInquiry inquiry) {
        inquiry.setReceivedDate(LocalDateTime.now());
        return inquiryRepository.save(inquiry);
    }

    /** Get all inquiries, newest first */
    public List<ContactInquiry> getAllInquiries() {
        return inquiryRepository.findAllByOrderByReceivedDateDesc();
    }

    /** Mark a message as read (called when admin opens it) */
    @Transactional
    public Optional<ContactInquiry> markRead(Long id) {
        return inquiryRepository.findById(id).map(inq -> {
            inq.setRead(true);
            return inquiryRepository.save(inq);
        });
    }

    /** Count unread messages */
    public long countUnread() {
        return inquiryRepository.countByReadFalse();
    }

    /**
     * Send a reply email to the fan and record it in the database.
     * Falls back gracefully if SMTP is not configured.
     */
    @Transactional
    public ContactInquiry sendReply(Long id, String replyText) {
        ContactInquiry inq = inquiryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inquiry not found: " + id));

        // Try to send email — log warning if SMTP not configured yet
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mikjcantrell@gmail.com");
            message.setTo(inq.getSenderEmail());
            message.setSubject("Re: " + inq.getSubject() + " — Presently Reading");
            message.setText(replyText + "\n\n---\nPresently Reading\nmikjcantrell@gmail.com");
            message.setReplyTo("mikjcantrell@gmail.com");
            mailSender.send(message);
            log.info("Reply sent to {} for inquiry #{}", inq.getSenderEmail(), id);
        } catch (Exception e) {
            log.warn("Could not send reply email (SMTP not configured?): {}", e.getMessage());
            throw new RuntimeException("Email send failed: " + e.getMessage());
        }

        inq.setRead(true);
        inq.setReplied(true);
        inq.setReplyText(replyText);
        inq.setRepliedDate(LocalDateTime.now());
        return inquiryRepository.save(inq);
    }
}
