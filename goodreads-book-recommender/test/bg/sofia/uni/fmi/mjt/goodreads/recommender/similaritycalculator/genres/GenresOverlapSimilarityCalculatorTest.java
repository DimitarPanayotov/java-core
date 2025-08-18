package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GenresOverlapSimilarityCalculatorTest {

    private final GenresOverlapSimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();

    @Test
    void testCalculateSimilarityWithEmptyGenres() {
        Book book1 = new Book("1", "Book1", "Author", "Desc", List.of(), 4.0, 100, "url");
        Book book2 = new Book("2", "Book2", "Author", "Desc", List.of("genre1"), 4.0, 100, "url");

        assertEquals(0.0, calculator.calculateSimilarity(book1, book2),
            "Should return 0 when one book has no genres");
    }

    @Test
    void testCalculateSimilarityWithCommonGenres() {
        Book book1 = new Book("1", "Book1", "Author", "Desc", List.of("genre1", "genre2"), 4.0, 100, "url");
        Book book2 = new Book("2", "Book2", "Author", "Desc", List.of("genre1", "genre3"), 4.0, 100, "url");

        assertEquals(0.5, calculator.calculateSimilarity(book1, book2),
            "Should calculate correct similarity for common genres");
    }

    @Test
    void testCalculateSimilarityWithIdenticalGenres() {
        Book book1 = new Book("1", "Book1", "Author", "Desc", List.of("genre1", "genre2"), 4.0, 100, "url");
        Book book2 = new Book("2", "Book2", "Author", "Desc", List.of("genre1", "genre2"), 4.0, 100, "url");

        assertEquals(1.0, calculator.calculateSimilarity(book1, book2),
            "Should return 1.0 for identical genres");
    }
}

