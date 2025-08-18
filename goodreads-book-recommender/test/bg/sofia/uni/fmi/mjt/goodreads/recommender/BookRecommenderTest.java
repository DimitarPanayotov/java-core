package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookRecommenderTest {

    @Mock
    private SimilarityCalculator calculatorMock;

    private BookRecommender recommender;
    private Book testBook;
    private Book similarBook1;
    private Book similarBook2;

    @BeforeEach
    void setUp() {
        testBook = new Book("1", "Test Book", "Author", "Description", List.of("genre1"), 4.5, 100, "url");
        similarBook1 = new Book("2", "Similar Book 1", "Author", "Similar description", List.of("genre1", "genre2"), 4.2, 80, "url2");
        similarBook2 = new Book("3", "Similar Book 2", "Author", "Another similar", List.of("genre1"), 4.0, 60, "url3");

        Set<Book> books = Set.of(testBook, similarBook1, similarBook2);
        recommender = new BookRecommender(books, calculatorMock);
    }

    @Test
    void testRecommendBooksWithNullBook() {
        assertTrue(recommender.recommendBooks(null, 5).isEmpty(),
            "Should return empty map for null book");
    }

    @Test
    void testRecommendBooksWithInvalidMaxN() {
        assertTrue(recommender.recommendBooks(testBook, 0).isEmpty(),
            "Should return empty map for maxN <= 0");
    }

    @Test
    void testRecommendBooksWithValidInput() {
        when(calculatorMock.calculateSimilarity(testBook, similarBook1)).thenReturn(0.8);
        when(calculatorMock.calculateSimilarity(testBook, similarBook2)).thenReturn(0.6);

        var recommendations = recommender.recommendBooks(testBook, 2);
        assertEquals(2, recommendations.size(), "Should return requested number of recommendations");
        assertTrue(recommendations.firstKey().equals(similarBook1), "Should return most similar book first");
    }

    @Test
    void testShutdown() {
        recommender.shutdown();
        assertDoesNotThrow(() -> recommender.shutdown(),
            "Shutdown should complete without exceptions");
    }
}