package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Set;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first.genres().isEmpty() || second.genres().isEmpty()) {
            return 0.0;
        }

        Set<String> secondGenres = new HashSet<>(second.genres());

        long commonGenresCount = first.genres()
            .stream()
            .filter(secondGenres::contains)
            .count();
        return commonGenresCount / (double) Math.min(first.genres().size(), second.genres().size());
    }
}