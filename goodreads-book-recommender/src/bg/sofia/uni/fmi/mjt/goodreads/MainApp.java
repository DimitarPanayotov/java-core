package bg.sofia.uni.fmi.mjt.goodreads;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.finder.BookFinder;
import bg.sofia.uni.fmi.mjt.goodreads.finder.BookFinderAPI;
import bg.sofia.uni.fmi.mjt.goodreads.finder.MatchOption;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.BookRecommender;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.BookRecommenderAPI;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite.CompositeSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions.TFIDFSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MainApp {
    private static final String BOOKS_FILE = "goodreads_data.csv";
    private static final String STOPWORDS_FILE = "stopwords_goodreads.txt";

    public static void main(String[] args) {
        Set<Book> books = loadBooks(BOOKS_FILE);
        TextTokenizer tokenizer = loadTokenizer(STOPWORDS_FILE);

        BookFinderAPI finder = new BookFinder(books, tokenizer);
        BookRecommenderAPI recommender = createRecommender(books, tokenizer);

        try {
            testBookFinder(finder);
            testBookRecommender(recommender, finder);
        } finally {
            if (recommender instanceof BookRecommender) {
                ((BookRecommender) recommender).shutdown();
            }
        }
    }

    private static Set<Book> loadBooks(String filename) {
        try (Reader reader = new FileReader(filename)) {
            Set<Book> books = BookLoader.load(reader);
            System.out.println("Successfully loaded " + books.size() + " books");
            return books;
        } catch (IOException | RuntimeException e) {
            System.err.println("Failed to load books: " + e.getMessage());
            throw new IllegalStateException("Could not load books data", e);
        }
    }

    private static TextTokenizer loadTokenizer(String filename) {
        try (Reader reader = new FileReader(filename)) {
            TextTokenizer tokenizer = new TextTokenizer(reader);
            System.out.println("Loaded tokenizer with " + tokenizer.stopwords().size() + " stopwords");
            return tokenizer;
        } catch (IOException e) {
            System.err.println("Failed to load stopwords: " + e.getMessage());
            throw new IllegalStateException("Could not load stopwords", e);
        }
    }

    private static BookRecommenderAPI createRecommender(Set<Book> books, TextTokenizer tokenizer) {
        SimilarityCalculator tfidf = new TFIDFSimilarityCalculator(books, tokenizer);
        SimilarityCalculator genres = new GenresOverlapSimilarityCalculator();

        Map<SimilarityCalculator, Double> calculators = Map.of(
            tfidf, 0.7,
            genres, 0.3
        );

        SimilarityCalculator composite = new CompositeSimilarityCalculator(calculators);
        return new BookRecommender(books, composite);
    }

    private static void testBookFinder(BookFinderAPI finder) {
        System.out.println("\n===  Testing Book Finder ===");

        String author = "J.K. Rowling";
        System.out.println("\nSearching for books by: " + author);
        List<Book> authorBooks = finder.searchByAuthor(author);
        authorBooks.forEach(b -> System.out.println(" - " + b.title()));

        Set<String> genres = Set.of("Fantasy", "Young Adult");
        System.out.println("\nSearching for books with genres: " + genres);
        List<Book> genreBooks = finder.searchByGenres(genres, MatchOption.MATCH_ANY);
        genreBooks.stream().limit(5).forEach(b ->
            System.out.println(" - " + b.title() + " (" + b.genres() + ")"));
    }

    private static void testBookRecommender(BookRecommenderAPI recommender, BookFinderAPI finder) {
        System.out.println("\n=== Testing Book Recommender ===");

        Book testBook = finder.searchByAuthor("J.R.R. Tolkien").stream()
            .filter(b -> b.title().contains("Hobbit"))
            .findFirst()
            .orElseGet(() -> finder.searchByAuthor("J.K. Rowling").get(0));

        System.out.println("\nGetting recommendations for:");
        System.out.println("Book: " + testBook.title() + " by " + testBook.author());
        System.out.println("Genres: " + testBook.genres());
        System.out.println("Description snippet: " +
            testBook.description().substring(0, Math.min(100, testBook.description().length())) + "...");

        System.out.println("\nCalculating recommendations...");
        SortedMap<Book, Double> recommendations = recommender.recommendBooks(testBook, 5);

        if (recommendations.isEmpty()) {
            System.out.println("No recommendations found");
        } else {
            System.out.println("\nTop Recommendations:");
            recommendations.forEach((book, score) ->
                System.out.printf("%.3f: %s by %s (Genres: %s)%n",
                    score, book.title(), book.author(), book.genres()));
        }
    }
}