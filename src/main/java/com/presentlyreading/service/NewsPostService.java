package com.presentlyreading.service;

import com.presentlyreading.model.NewsPost;
import com.presentlyreading.repository.NewsPostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsPostService {

    private final NewsPostRepository repository;

    public NewsPostService(NewsPostRepository repository) {
        this.repository = repository;
    }

    public List<NewsPost> getPublicNews() {
        return repository.findByPublishedTrueOrderByCreatedAtDesc();
    }

    public List<NewsPost> getAllNewsForAdmin() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<NewsPost> getNewsById(Long id) {
        return repository.findById(id);
    }

    public NewsPost createNews(NewsPost post) {
        return repository.save(post);
    }

    public Optional<NewsPost> updateNews(Long id, NewsPost updatedPost) {
        return repository.findById(id).map(existing -> {
            existing.setTitle(updatedPost.getTitle());
            existing.setContent(updatedPost.getContent());
            existing.setImageUrl(updatedPost.getImageUrl());
            existing.setPublished(updatedPost.isPublished());
            return repository.save(existing);
        });
    }

    public boolean deleteNews(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
