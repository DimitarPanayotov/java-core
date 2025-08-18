package bg.sofia.uni.fmi.mjt.goodreads.book;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testOfWithValidTokens() {
        String[] tokens = {
            "1", "Title", "Author", "Description", "['genre1','genre2']", "4.5", "100", "url"
        };
        Book book = Book.of(tokens);

        assertEquals("1", book.ID());
        assertEquals("Title", book.title());
        assertEquals(2, book.genres().size());
        assertEquals(4.5, book.rating());
        assertEquals(100, book.ratingCount());
    }

    @Test
    void testOfWithNullTokens() {
        assertThrows(IllegalArgumentException.class, () -> Book.of(null),
            "Should throw for null tokens");
    }

    @Test
    void testOfWithInvalidTokenCount() {
        String[] invalidTokens = {"1", "Title"};
        assertThrows(IllegalArgumentException.class, () -> Book.of(invalidTokens),
            "Should throw for incorrect number of tokens");
    }

    @Test
    void testOfWithEmptyGenres() {
        String[] tokens = {
            "1", "Title", "Author", "Description", "", "4.5", "100", "url"
        };
        Book book = Book.of(tokens);
        assertTrue(book.genres().isEmpty(), "Should handle empty genres");
    }

    @Test
    void testOfWithFormattedRatingCount() {
        String[] tokens = {
            "1", "Title", "Author", "Description", "['genre']", "4.5", "1,000", "url"
        };
        Book book = Book.of(tokens);
        assertEquals(1000, book.ratingCount(), "Should handle formatted rating count");
    }
}

