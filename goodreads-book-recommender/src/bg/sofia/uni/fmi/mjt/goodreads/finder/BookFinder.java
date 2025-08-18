package bg.sofia.uni.fmi.mjt.goodreads.finder;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookFinder implements BookFinderAPI {
    private Set<Book> books;
    private TextTokenizer tokenizer;

    public BookFinder(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.tokenizer = tokenizer;
    }

    public Set<Book> allBooks() {
        return Collections.unmodifiableSet(books);
    }

    @Override
    public List<Book> searchByAuthor(String authorName) {
        if (authorName == null || authorName.isBlank()) {
            throw new IllegalArgumentException("Author name cannot be null or empty");
        }

        return books.stream()
            .filter(book -> book.author().toLowerCase().equals(authorName.toLowerCase()))
            .toList();
    }

    @Override
    public Set<String> allGenres() {
        return books.stream()
            .flatMap(book -> book.genres().stream())
            .collect(Collectors.toSet());
    }

    @Override
    public List<Book> searchByGenres(Set<String> genres, MatchOption option) {
        if (genres == null || option == null) {
            throw new IllegalArgumentException("Genres set or option cannot be null!");
        }
        return books.stream()
            .filter(book -> {
                Set<String> bookGenres = Set.copyOf(book.genres());
                if (option == MatchOption.MATCH_ALL) {
                    return bookGenres.containsAll(genres);
                } else {
                    return genres.stream().anyMatch(bookGenres::contains);
                }
            })
            .toList();
    }

    @Override
    public List<Book> searchByKeywords(Set<String> keywords, MatchOption option) {
        if (keywords == null || option == null) {
            throw new IllegalArgumentException("Keywords set or option cannot be null!");
        }

        Set<String> normalizedKeywords = keywords.stream()
            .flatMap(kw -> tokenizer.tokenize(kw).stream())
            .collect(Collectors.toSet());

        return books.stream()
            .filter(book -> {
                Set<String> bookTokens = Stream.concat(
                    tokenizer.tokenize(book.title()).stream(),
                    tokenizer.tokenize(book.description()).stream()
                ).collect(Collectors.toSet());

                if (option == MatchOption.MATCH_ALL) {
                    return bookTokens.containsAll(normalizedKeywords);
                } else {
                    return normalizedKeywords.stream().anyMatch(bookTokens::contains);
                }
            })
            .toList();
    }

}