package com.presentlyreading.service;

import com.presentlyreading.model.NewsletterSubscriber;
import com.presentlyreading.repository.NewsletterSubscriberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NewsletterService {

    private final NewsletterSubscriberRepository subscriberRepository;

    /**
     * Subscribe an email address.
     *
     * @return true if newly subscribed, false if already on the list
     */
    public boolean subscribe(String email) {
        if (subscriberRepository.existsByEmail(email)) {
            return false; // already subscribed — not an error
        }
        NewsletterSubscriber sub = new NewsletterSubscriber();
        sub.setEmail(email.toLowerCase().trim());
        sub.setSubscribedAt(LocalDateTime.now());
        sub.setActive(true);
        subscriberRepository.save(sub);
        return true;
    }
}
