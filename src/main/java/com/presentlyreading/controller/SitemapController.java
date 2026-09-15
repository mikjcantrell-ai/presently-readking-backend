package com.presentlyreading.controller;

import com.presentlyreading.model.Book;
import com.presentlyreading.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SitemapController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/sitemap.xml", produces = "application/xml")
    public String getSitemap() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Add static routes
        addUrl(xml, "https://presentlyreading.com/", "1.0");
        addUrl(xml, "https://presentlyreading.com/music", "0.9");
        addUrl(xml, "https://presentlyreading.com/about", "0.8");
        addUrl(xml, "https://presentlyreading.com/contact", "0.8");

        // Add dynamic routes for all books (quotes pages)
        List<Book> books = bookService.getAllBooks();
        for (Book book : books) {
            if (book.getId() != null) {
                addUrl(xml, "https://presentlyreading.com/quotes/" + book.getId(), "0.9");
            }
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    private void addUrl(StringBuilder xml, String url, String priority) {
        xml.append("  <url>\n");
        xml.append("    <loc>").append(url).append("</loc>\n");
        xml.append("    <changefreq>weekly</changefreq>\n");
        xml.append("    <priority>").append(priority).append("</priority>\n");
        xml.append("  </url>\n");
    }
}
