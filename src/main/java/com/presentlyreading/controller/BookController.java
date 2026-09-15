package com.presentlyreading.controller;

import com.presentlyreading.model.Book;
import com.presentlyreading.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for Presently Reading books.
 *
 * <p>Public endpoints (GET) are open to all.
 * Write endpoints (POST/PUT/DELETE) require HTTP Basic admin auth
 * (configured in SecurityConfig).
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /** GET /api/books — all books, ordered by displayOrder */
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /** GET /api/books/featured — books shown on the home page */
    @GetMapping("/featured")
    public List<Book> getFeaturedBooks() {
        return bookService.getFeaturedBooks();
    }

    /** GET /api/books/{id} — single book by ID */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET /api/books/genre/{genre} — filter by genre tag */
    @GetMapping("/genre/{genre}")
    public List<Book> getBooksByGenre(@PathVariable String genre) {
        return bookService.getBooksByGenre(genre);
    }

    /** POST /api/books — create a new book (admin only) */
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    /** PUT /api/books/{id} — update a book (admin only) */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/books/{id} — remove a book (admin only) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    /** POST /api/books/batch-delete — delete multiple books (admin only) */
    @PostMapping("/batch-delete")
    public ResponseEntity<Void> batchDelete(@RequestBody java.util.List<Long> ids) {
        bookService.batchDelete(ids);
        return ResponseEntity.ok().build();
    }
}
