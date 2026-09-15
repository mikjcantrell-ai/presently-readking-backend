package com.presentlyreading.controller;

import com.presentlyreading.model.Quote;
import com.presentlyreading.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * REST controller for book quotes.
 *
 * <p>Public:
 *   GET /api/books/{bookId}/quotes
 *
 * <p>Admin (HTTP Basic required):
 *   POST   /api/books/{bookId}/quotes         — add a quote block
 *   PUT    /api/quotes/{id}                   — edit a quote block
 *   DELETE /api/quotes/{id}                   — delete a quote block
 *   PUT    /api/books/{bookId}/quotes/reorder — bulk reorder {id: order}
 */
@RestController
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    /** GET /api/books/{bookId}/quotes */
    @GetMapping("/api/books/{bookId}/quotes")
    public List<Quote> getQuotes(@PathVariable Long bookId) {
        return quoteService.getQuotesByBook(bookId);
    }

    /** POST /api/books/{bookId}/quotes — admin: add a block */
    @PostMapping("/api/books/{bookId}/quotes")
    public Quote createQuote(@PathVariable Long bookId, @RequestBody Quote quote) {
        return quoteService.createQuote(bookId, quote);
    }

    /** PUT /api/quotes/{id} — admin: edit a block */
    @PutMapping("/api/quotes/{id}")
    public ResponseEntity<Quote> updateQuote(@PathVariable Long id, @RequestBody Quote quote) {
        return quoteService.updateQuote(id, quote)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/quotes/{id} — admin: delete a block */
    @DeleteMapping("/api/quotes/{id}")
    public ResponseEntity<Void> deleteQuote(@PathVariable Long id) {
        quoteService.deleteQuote(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PUT /api/books/{bookId}/quotes/reorder — admin: bulk reorder.
     * Body: { "10": 1, "11": 2, "12": 3, … }
     */
    @PutMapping("/api/books/{bookId}/quotes/reorder")
    public ResponseEntity<Void> reorder(@PathVariable Long bookId,
                                        @RequestBody Map<Long, Integer> orderMap) {
        quoteService.reorder(orderMap);
        return ResponseEntity.ok().build();
    }
}
