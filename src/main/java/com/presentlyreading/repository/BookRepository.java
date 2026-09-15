package com.presentlyreading.repository;

import com.presentlyreading.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    /** Featured books for the home page, sorted by admin-set display order. */
    List<Book> findByFeaturedStatusTrueOrderByDisplayOrderAsc();

    /** Filter by genre tag. */
    List<Book> findByGenreContainingIgnoreCase(String genre);

    /** All books sorted by display order then ID. */
    List<Book> findAllByOrderByDisplayOrderAscIdAsc();
}
