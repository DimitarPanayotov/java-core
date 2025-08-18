package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.Map;

class TFIDFSimilarityCalculatorTest {

    private TextTokenizer tokenizerMock;
    private TFIDFSimilarityCalculator calculator;

    private Book bookX;
    private Book bookY;
    private Book bookZ;

    @BeforeEach
    void setup() {
        tokenizerMock = mock(TextTokenizer.class);

        bookX = Book.of(new String[] {
            "X", "TitleX", "AuthorX", "academy superhero club superhero", "['fantasy','action']", "4.5", "1,000", "urlX"
        });
        bookY = Book.of(new String[] {
            "Y", "TitleY", "AuthorY", "superhero mission save club", "['action','adventure']", "4.2", "500", "urlY"
        });
        bookZ = Book.of(new String[] {
            "Z", "TitleZ", "AuthorZ", "crime murder mystery club", "['crime','thriller']", "4.0", "750", "urlZ"
        });

        when(tokenizerMock.tokenize(bookX.description()))
            .thenReturn(List.of("academy", "superhero", "club", "superhero"));
        when(tokenizerMock.tokenize(bookY.description()))
            .thenReturn(List.of("superhero", "mission", "save", "club"));
        when(tokenizerMock.tokenize(bookZ.description()))
            .thenReturn(List.of("crime", "murder", "mystery", "club"));

        calculator = new TFIDFSimilarityCalculator(Set.of(bookX, bookY, bookZ), tokenizerMock);
    }

    @Test
    void testComputeTF() {
        Map<String, Double> tf = calculator.computeTF(bookX);

        assertEquals(0.25, tf.get("academy"), 0.0001);
        assertEquals(0.5, tf.get("superhero"), 0.0001);
        assertEquals(0.25, tf.get("club"), 0.0001);
    }

    @Test
    void testComputeIDF() {
        Map<String, Double> idfScores = calculator.getIdfScores();

        assertEquals(Math.log10(3.0 / 2), idfScores.get("superhero"), 0.0001);
        assertEquals(0.0, idfScores.get("club"), 0.0001);
        assertEquals(Math.log10(3.0 / 1), idfScores.get("academy"), 0.0001);
    }

    @Test
    void testComputeTFIDF() {
        Map<String, Double> tfidf = calculator.computeTFIDF(bookX);

        double expectedSuperhero = 0.5 * Math.log10(3.0 / 2);
        assertEquals(expectedSuperhero, tfidf.get("superhero"), 0.0001);

        assertEquals(0.0, tfidf.get("club"), 0.0001);

        double expectedAcademy = 0.25 * Math.log10(3.0 / 1);
        assertEquals(expectedAcademy, tfidf.get("academy"), 0.0001);
    }

    @Test
    void testCalculateSimilarity_SameBook() {
        double similarity = calculator.calculateSimilarity(bookX, bookX);

        assertEquals(1.0, similarity, 0.0001);
    }

    @Test
    void testCalculateSimilarity_DifferentBooks() {
        double similarityXY = calculator.calculateSimilarity(bookX, bookY);
        double similarityXZ = calculator.calculateSimilarity(bookX, bookZ);

        assertTrue(similarityXY > 0);
        assertEquals(0, similarityXZ);

        Book bookEmpty = Book.of(new String[] {
            "Empty", "TitleEmpty", "AuthorEmpty", "", "[]", "0", "0", "urlEmpty"
        });
        when(tokenizerMock.tokenize(bookEmpty.description())).thenReturn(List.of());

        double similarityWithEmpty = calculator.calculateSimilarity(bookX, bookEmpty);
        assertEquals(0.0, similarityWithEmpty, 0.0001);
    }
}

