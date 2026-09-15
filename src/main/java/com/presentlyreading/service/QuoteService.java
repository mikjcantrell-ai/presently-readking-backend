package com.presentlyreading.service;

import com.presentlyreading.model.Quote;
import com.presentlyreading.model.Book;
import com.presentlyreading.repository.QuoteRepository;
import com.presentlyreading.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final BookRepository  bookRepository;

    /** All quote blocks for a book, in display order. */
    public List<Quote> getQuotesByBook(Long bookId) {
        return quoteRepository.findByBookIdOrderByDisplayOrderAsc(bookId);
    }

    /** Create a new quote block attached to a book. */
    @Transactional
    public Quote createQuote(Long bookId, Quote quote) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found: " + bookId));
        quote.setBook(book);
        if (quote.getDisplayOrder() == 0) {
            int maxOrder = quoteRepository.findByBookIdOrderByDisplayOrderAsc(bookId)
                    .stream().mapToInt(Quote::getDisplayOrder).max().orElse(0);
            quote.setDisplayOrder(maxOrder + 1);
        }
        return quoteRepository.save(quote);
    }

    /** Update an existing quote block's label, type, content, or order. */
    @Transactional
    public Optional<Quote> updateQuote(Long quoteId, Quote updated) {
        return quoteRepository.findById(quoteId).map(existing -> {
            existing.setSectionLabel(updated.getSectionLabel());
            existing.setSectionType(updated.getSectionType());
            existing.setContent(updated.getContent());
            existing.setDisplayOrder(updated.getDisplayOrder());
            return quoteRepository.save(existing);
        });
    }

    /** Delete a single quote block by ID. */
    @Transactional
    public void deleteQuote(Long quoteId) {
        quoteRepository.deleteById(quoteId);
    }

    /**
     * Bulk-reorder: accepts a map of {quoteId -> newDisplayOrder} and
     * persists all in one transaction.
     */
    @Transactional
    public void reorder(Map<Long, Integer> orderMap) {
        orderMap.forEach((id, order) ->
            quoteRepository.findById(id).ifPresent(l -> {
                l.setDisplayOrder(order);
                quoteRepository.save(l);
            })
        );
    }

    // Legacy helpers kept for DataSeeder compatibility
    public Quote saveQuote(Quote quote) { return quoteRepository.save(quote); }
    public void deleteQuotesByBook(Long bookId) { quoteRepository.deleteByBookId(bookId); }
}
