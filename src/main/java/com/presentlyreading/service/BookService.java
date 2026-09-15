package com.presentlyreading.service;

import com.presentlyreading.model.Book;
import com.presentlyreading.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final com.presentlyreading.repository.QuoteRepository quoteRepository;

    /** All books ordered by displayOrder then id. */
    public List<Book> getAllBooks() {
        return bookRepository.findAllByOrderByDisplayOrderAscIdAsc();
    }

    /** Books marked featured=true, for the home page. */
    public List<Book> getFeaturedBooks() {
        return bookRepository.findByFeaturedStatusTrueOrderByDisplayOrderAsc();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(Long id, Book updated) {
        return bookRepository.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setPurchaseUrl(updated.getPurchaseUrl());
            existing.setGoodreadsUrl(updated.getGoodreadsUrl());
            existing.setImageUrl(updated.getImageUrl());
            existing.setGenre(updated.getGenre());
            existing.setReleaseYear(updated.getReleaseYear());
            existing.setAuthorName(updated.getAuthorName());
            existing.setFeaturedStatus(updated.isFeaturedStatus());
            existing.setReadingStatus(updated.getReadingStatus());
            existing.setDisplayOrder(updated.getDisplayOrder());
            existing.setDescription(updated.getDescription());
            existing.setFullReview(updated.getFullReview());
            return bookRepository.save(existing);
        });
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteBook(Long id) {
        quoteRepository.deleteByBookId(id);
        bookRepository.deleteById(id);
    }
    
    @org.springframework.transaction.annotation.Transactional
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            quoteRepository.deleteByBookId(id);
        }
        bookRepository.deleteAllById(ids);
    }
}
