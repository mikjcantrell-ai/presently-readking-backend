package com.presentlyreading;

import com.presentlyreading.model.Quote;
import com.presentlyreading.model.Quote.SectionType;
import com.presentlyreading.model.Book;
import com.presentlyreading.model.SiteContent;
import com.presentlyreading.repository.QuoteRepository;
import com.presentlyreading.repository.BookRepository;
import com.presentlyreading.repository.AdminUserRepository;
import com.presentlyreading.model.AdminUser;
import com.presentlyreading.service.AuthorProfileService;
import com.presentlyreading.service.SiteContentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final QuoteRepository quoteRepository;
    private final SiteContentService siteContentService;
    private final AuthorProfileService authorProfileService;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.user.name:admin}")
    private String defaultAdminUsername;

    @Value("${spring.security.user.password:T@ylor55}")
    private String defaultAdminPassword;

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedSiteContent();
        seedAuthorProfile();
        if (bookRepository.count() > 0) {
            log.info("DataSeeder: database already seeded, skipping.");
            return;
        }
        log.info("DataSeeder: seeding book reviews...");

        Book book1 = seedBook(1, "The Secret History",
                "Donna Tartt",
                "Dark Academia",
                null, null, null,
                "Dark academia at its finest. A beautifully written, slow-burn psychological thriller that I simply could not put down.",
                true);
        seedQuote(book1, "Favorite Quote", Quote.SectionType.VERSE, 1, "\"Beauty is rarely soft or consolatory. Quite the contrary. Genuine beauty is always quite arousing.\"");

        Book book2 = seedBook(2, "Tomorrow, and Tomorrow, and Tomorrow",
                "Gabrielle Zevin",
                "Contemporary",
                null, null, null,
                "A profound novel about love, art, and video games. The character dynamics are incredibly complex and moving.",
                true);
        seedQuote(book2, "Favorite Quote", Quote.SectionType.VERSE, 1, "\"What is a game? It's tomorrow, and tomorrow, and tomorrow. It's the possibility of infinite rebirth, infinite redemption.\"");

        log.info("DataSeeder: books seeded successfully.");
    }

    private Book seedBook(int displayOrder, String title, String authorName, String genre,
                           String purchaseUrl, String goodreadsUrl, String imageUrl,
                           String description, boolean featured) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthorName(authorName);
        book.setGenre(genre);
        book.setPurchaseUrl(purchaseUrl);
        book.setGoodreadsUrl(goodreadsUrl);
        book.setImageUrl(imageUrl);
        book.setReleaseYear(2024);
        book.setFeaturedStatus(featured);
        book.setDisplayOrder(displayOrder);
        book.setDescription(description);
        return bookRepository.save(book);
    }

    private void seedAdminUser() {
        if (adminUserRepository.count() == 0) {
            AdminUser user = new AdminUser();
            user.setUsername(defaultAdminUsername);
            user.setPassword(passwordEncoder.encode(defaultAdminPassword));
            adminUserRepository.save(user);
            log.info("DataSeeder: Seeded default admin user.");
        }
    }

    private int seedQuote(Book book, String label, Quote.SectionType type, int order, String content) {
        Quote quote = new Quote();
        quote.setBook(book);
        quote.setSectionLabel(label);
        quote.setSectionType(type);
        quote.setContent(content);
        quote.setDisplayOrder(order);
        quoteRepository.save(quote);
        return order + 1;
    }

    private void seedAuthorProfile() {
        com.presentlyreading.model.AuthorProfile profile = authorProfileService.getProfile();
        if (profile.getWebsiteUrl() == null || profile.getWebsiteUrl().isBlank()) {
            profile.setName("Melody");
            profile.setWebsiteUrl("");
            profile.setTagline("Books · Coffee · Aesthetic");
            authorProfileService.updateProfile(profile);
        }
    }

    private void seedSiteContent() {
        List<SiteContent> defaults = List.of(
            new SiteContent("about_text_1",   "Paragraph 1",     "About",      "Presently Reading is an AI-crafted musical project steeped in the sights, sounds, and soul of the industrial night. We write books that smell like smoke and marigolds, that taste like gasoline and regret, that sound like striking a match in the dead of night and watching it burn bright."),
            new SiteContent("about_text_2",   "Paragraph 2",     "About",      "Drawing from the deep wells of indie rock, industrial synth, and atmospheric pop, Presently Reading creates sonic landscapes where every note carries the weight of a burned bridge and the lightness of a spark catching in the wind."),
            new SiteContent("about_tagline",  "About Tagline",   "About",      "Born from Matches & Gasoline"),
            new SiteContent("about_quote",    "About Quote",     "About",      "\"You were marigold and gasoline, burned so bright, so in between living and letting go.\""),
            new SiteContent("about_quote_cit","About Citation",  "About",      "— Marigold and Gasoline"),

            new SiteContent("genre_1_icon",   "Genre 1 Icon",    "Genres",     "🐉"),
            new SiteContent("genre_1_title",  "Genre 1 Title",   "Genres",     "Fantasy"),
            new SiteContent("genre_1_desc",   "Genre 1 Desc",    "Genres",     "Epic world-building, magical systems, and high stakes. If there are dragons or hidden kingdoms, I'm already hooked."),
            
            new SiteContent("genre_2_icon",   "Genre 2 Icon",    "Genres",     "💌"),
            new SiteContent("genre_2_title",  "Genre 2 Title",   "Genres",     "Romance"),
            new SiteContent("genre_2_desc",   "Genre 2 Desc",    "Genres",     "From slow-burn enemies to lovers, to cozy small-town romances. I'm a sucker for a beautiful love story with a guaranteed HEA."),
            
            new SiteContent("genre_3_icon",   "Genre 3 Icon",    "Genres",     "🖋️"),
            new SiteContent("genre_3_title",  "Genre 3 Title",   "Genres",     "Dark Academia"),
            new SiteContent("genre_3_desc",   "Genre 3 Desc",    "Genres",     "Old libraries, mysterious boarding schools, and quoteal prose. The perfect aesthetic for a rainy autumn afternoon."),
            
            new SiteContent("genre_4_icon",   "Genre 4 Icon",    "Genres",     "🌿"),
            new SiteContent("genre_4_title",  "Genre 4 Title",   "Genres",     "Literary Fiction"),
            new SiteContent("genre_4_desc",   "Genre 4 Desc",    "Genres",     "Character-driven stories that make you stop and reflect on life, relationships, and the beautifully mundane moments.")
        );
        for (SiteContent c : defaults) {
            siteContentService.seedIfEmpty(List.of(c));
        }
    }
}
