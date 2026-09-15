package com.presentlyreading.repository;

import com.presentlyreading.model.NewsPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsPostRepository extends JpaRepository<NewsPost, Long> {
    List<NewsPost> findByPublishedTrueOrderByCreatedAtDesc();
    List<NewsPost> findAllByOrderByCreatedAtDesc();
}
