package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
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
class BookFinderTest {

    @Mock
    private TextTokenizer tokenizerMock;

    private BookFinder finder;
    private Book book1;
    private Book book2;
    private Book book3;

    @BeforeEach
    void setUp() {
        book1 = new Book("1", "Java Programming", "Author A", "Learn Java programming", List.of("programming", "tech"), 4.5, 100, "url1");
        book2 = new Book("2", "Python Basics", "Author B", "Introduction to Python", List.of("programming", "beginner"), 4.0, 80, "url2");
        book3 = new Book("3", "Cooking Recipes", "Author C", "Delicious recipes", List.of("cooking", "food"), 4.2, 120, "url3");

        Set<Book> books = Set.of(book1, book2, book3);
        finder = new BookFinder(books, tokenizerMock);
    }

    @Test
    void testAllBooks() {
        assertEquals(3, finder.allBooks().size(),
            "Should return all books");
    }

    @Test
    void testAllGenres() {
        Set<String> genres = finder.allGenres();
        assertTrue(genres.containsAll(Set.of("programming", "tech", "beginner", "cooking", "food")),
            "Should return all genres from all books");
    }

    @Test
    void testSearchByAuthorWithNull() {
        assertThrows(IllegalArgumentException.class, () -> finder.searchByAuthor(null),
            "Should throw for null author");
    }

    @Test
    void testSearchByAuthor() {
        List<Book> result = finder.searchByAuthor("Author A");
        assertEquals(1, result.size());
        assertEquals(book1, result.get(0));
    }

    @Test
    void testSearchByGenresWithNull() {
        assertThrows(IllegalArgumentException.class, () -> finder.searchByGenres(null, MatchOption.MATCH_ANY),
            "Should throw for null genres");
        assertThrows(IllegalArgumentException.class, () -> finder.searchByGenres(Set.of("tech"), null),
            "Should throw for null option");
    }

    @Test
    void testSearchByGenresMatchAll() {
        List<Book> result = finder.searchByGenres(Set.of("programming"), MatchOption.MATCH_ALL);
        assertEquals(2, result.size());
        assertTrue(result.containsAll(List.of(book1, book2)));
    }

    @Test
    void testSearchByKeywords() {
        when(tokenizerMock.tokenize(anyString())).thenAnswer(invocation -> {
            String arg = invocation.getArgument(0);
            switch (arg) {
                case "Java Programming":
                    return List.of("java", "programming");
                case "Learn Java programming":
                    return List.of("learn", "java", "programming", "language");
                case "Python Basics":
                    return List.of("python", "basics");
                case "Introduction to Python":
                    return List.of("introduction", "to", "python", "language", "programming");
                case "programming":
                    return List.of("programming");
                case "language":
                    return List.of("language");
                default:
                    return List.of();
            }
        });

        List<Book> result = finder.searchByKeywords(Set.of("programming", "language"), MatchOption.MATCH_ALL);
        assertEquals(2, result.size(), "Should find both programming books with MATCH_ALL");
        assertTrue(result.containsAll(List.of(book1, book2)), "Should return both books containing all keywords");
    }

}

