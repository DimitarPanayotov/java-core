package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {
    private final Set<Book> books;
    private final TextTokenizer tokenizer;

    private final ConcurrentHashMap<Book, Map<String, Double>> tfidfCache = new ConcurrentHashMap<>();
    private final Map<String, Double> idfScores;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        this.books = books;
        this.tokenizer = tokenizer;
        this.idfScores = computeIDF();
    }

    @Override
    public double calculateSimilarity(Book first, Book second) {
        Map<String, Double> tfIdfScoresFirst = tfidfCache.computeIfAbsent(first, this::computeTFIDF);
        Map<String, Double> tfIdfScoresSecond = tfidfCache.computeIfAbsent(second, this::computeTFIDF);

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        Map<String, Double> tf = computeTF(book);

        Map<String, Double> tfidf = new HashMap<>();
        for (String word : tf.keySet()) {
            double idf = idfScores.getOrDefault(word, 0.0);
            double tfidfValue = tf.get(word) * idf;
            tfidf.put(word, tfidfValue);
        }

        return tfidf;
    }

    public Map<String, Double> computeTF(Book book) {
        List<String> words = tokenizer.tokenize(book.description());
        Map<String, Long> freq = words.stream()
            .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        int totalWords = words.size();

        return freq.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue() / (double) totalWords
            ));
    }

    private Map<String, Double> computeIDF() {
        Map<String, Long> docFreq = new HashMap<>();
        int totalBooks = books.size();

        for (Book book : books) {
            Set<String> uniqueWords = new HashSet<>(tokenizer.tokenize(book.description()));

            for (String word : uniqueWords) {
                docFreq.put(word, docFreq.getOrDefault(word, 0L) + 1);
            }
        }

        Map<String, Double> idfScores = new HashMap<>();
        for (Map.Entry<String, Long> entry : docFreq.entrySet()) {
            double idf = Math.log10((double) totalBooks / entry.getValue());
            idfScores.put(entry.getKey(), idf);
        }

        return idfScores;
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        if (magnitudeFirst == 0.0 || magnitudeSecond == 0.0) {
            return 0.0;
        }

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
            .mapToDouble(word -> first.get(word) * second.get(word))
            .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
            .mapToDouble(v -> v * v)
            .sum();

        return Math.sqrt(squaredMagnitude);
    }

    public Map<String, Double> getIdfScores() {
        return idfScores;
    }
}
