package com.presentlyreading.controller;

import com.presentlyreading.model.BookImportDto;
import com.presentlyreading.service.GoogleBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/import")
public class BookImportController {

    @Autowired
    private GoogleBooksService googleBooksService;

    @GetMapping("/books")
    public ResponseEntity<List<BookImportDto>> searchBooks(@RequestParam String query) {
        try {
            return ResponseEntity.ok(googleBooksService.searchBooks(query));
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            System.err.println("Google Books API Error: " + e.getMessage());
            return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_GATEWAY).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
