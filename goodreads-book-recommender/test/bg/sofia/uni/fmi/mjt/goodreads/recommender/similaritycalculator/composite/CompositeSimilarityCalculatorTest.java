package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompositeSimilarityCalculatorTest {

    @Mock
    private SimilarityCalculator calculator1;

    @Mock
    private SimilarityCalculator calculator2;

    @Mock
    private Book book1;

    @Mock
    private Book book2;

    @Test
    void testConstructorWithNullMap() {
        assertThrows(IllegalArgumentException.class, () -> new CompositeSimilarityCalculator(null),
            "Should throw IllegalArgumentException for null map");
    }

    @Test
    void testConstructorWithEmptyMap() {
        assertThrows(IllegalArgumentException.class, () -> new CompositeSimilarityCalculator(Map.of()),
            "Should throw IllegalArgumentException for empty map");
    }

    @Test
    void testCalculateSimilarity() {
        when(calculator1.calculateSimilarity(book1, book2)).thenReturn(0.5);
        when(calculator2.calculateSimilarity(book1, book2)).thenReturn(0.3);

        Map<SimilarityCalculator, Double> calculators = Map.of(
            calculator1, 0.7,
            calculator2, 0.3
        );
        CompositeSimilarityCalculator composite = new CompositeSimilarityCalculator(calculators);

        double expected = 0.5 * 0.7 + 0.3 * 0.3;
        assertEquals(expected, composite.calculateSimilarity(book1, book2), 0.001,
            "Should return weighted sum of similarity scores");
    }
}