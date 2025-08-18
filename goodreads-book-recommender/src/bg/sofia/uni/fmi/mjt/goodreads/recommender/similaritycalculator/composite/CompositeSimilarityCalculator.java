package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Map;

public class CompositeSimilarityCalculator implements SimilarityCalculator {
    private final Map<SimilarityCalculator, Double> similarityCalculatorMap;

    public CompositeSimilarityCalculator(Map<SimilarityCalculator, Double> similarityCalculatorMap) {
        if (similarityCalculatorMap == null || similarityCalculatorMap.isEmpty()) {
            throw new IllegalArgumentException("Similarity calculator map must not be null or empty");
        }
        this.similarityCalculatorMap = Map.copyOf(similarityCalculatorMap);
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        double sum = 0.0;
        for (var entry : similarityCalculatorMap.entrySet()) {
            SimilarityCalculator calculator = entry.getKey();
            double weight = entry.getValue();

            sum += calculator.calculateSimilarity(first, second) * weight;
        }
        return sum;
    }

}