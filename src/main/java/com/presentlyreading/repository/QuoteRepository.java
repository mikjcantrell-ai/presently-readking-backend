package com.presentlyreading.repository;

import com.presentlyreading.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

    /**
     * Fetch all quote blocks for a given book, ordered for display.
     * Used by QuoteController GET /api/books/{bookId}/quotes.
     */
    List<Quote> findByBookIdOrderByDisplayOrderAsc(Long bookId);

    /** Remove all quotes for a book (e.g. when replacing full quote content). */
    void deleteByBookId(Long bookId);
}
