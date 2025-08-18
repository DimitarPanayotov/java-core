package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class TextTokenizerTest {

    @Test
    void testTokenizeWithStopwords() {
        String stopwords = "the\nand\nof";
        TextTokenizer tokenizer = new TextTokenizer(new StringReader(stopwords));

        List<String> tokens = tokenizer.tokenize("The quick brown fox and the lazy dog");
        assertEquals(List.of("quick", "brown", "fox", "lazy", "dog"), tokens,
            "Should remove stopwords and lowercase tokens");
    }

    @Test
    void testTokenizeWithPunctuation() {
        TextTokenizer tokenizer = new TextTokenizer(new StringReader(""));
        List<String> tokens = tokenizer.tokenize("Hello, world! How's it going?");
        assertEquals(List.of("hello", "world", "hows", "it", "going"), tokens,
            "Should handle punctuation correctly");
    }

    @Test
    void testTokenizeEmptyInput() {
        TextTokenizer tokenizer = new TextTokenizer(new StringReader(""));
        assertTrue(tokenizer.tokenize(null).isEmpty(),
            "Should return empty list for null input");
        assertTrue(tokenizer.tokenize("").isEmpty(),
            "Should return empty list for empty input");
        assertTrue(tokenizer.tokenize("   ").isEmpty(),
            "Should return empty list for whitespace input");
    }

    @Test
    void testStopwords() {
        String stopwords = "word1\nword2\nword3";
        TextTokenizer tokenizer = new TextTokenizer(new StringReader(stopwords));
        assertEquals(Set.of("word1", "word2", "word3"), tokenizer.stopwords(),
            "Should return correct stopwords set");
    }
}

