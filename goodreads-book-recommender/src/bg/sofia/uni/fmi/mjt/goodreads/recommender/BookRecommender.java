package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BookRecommender implements BookRecommenderAPI {
    private static final int MAX_BOOKS_TO_COMPARE = 2000;
    private static final double MIN_SIMILARITY_THRESHOLD = 0.1;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private final List<Book> candidateBooks;
    private final SimilarityCalculator calculator;
    private final ExecutorService executor;

    public BookRecommender(Set<Book> books, SimilarityCalculator calculator) {
        this.calculator = calculator;
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        this.candidateBooks = books.parallelStream()
            .filter(b -> !b.genres().isEmpty())
            .filter(b -> b.description() != null && !b.description().isBlank())
            .limit(MAX_BOOKS_TO_COMPARE)
            .collect(Collectors.toList());
    }

    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        if (origin == null || maxN <= 0) {
            return new TreeMap<>(Collections.reverseOrder());
        }

        long startTime = System.currentTimeMillis();
        System.out.printf("Calculating recommendations using %d threads...%n", THREAD_POOL_SIZE);

        ConcurrentHashMap<Book, Double> similarityScores = new ConcurrentHashMap<>();

        CountDownLatch completionLatch = new CountDownLatch(candidateBooks.size());
        AtomicInteger processedCount = new AtomicInteger(0);

        for (Book candidate : candidateBooks) {
            if (candidate.equals(origin)) {
                completionLatch.countDown();
                continue;
            }

            executor.submit(() -> {
                try {
                    double similarity = calculator.calculateSimilarity(origin, candidate);
                    if (similarity > MIN_SIMILARITY_THRESHOLD) {
                        similarityScores.put(candidate, similarity);
                    }

                    int count = processedCount.incrementAndGet();
                    if (count % 100 == 0) {
                        System.out.printf("Processed %d/%d books (%.1f%%)%n",
                            count, candidateBooks.size(),
                            (count * 100.0 / candidateBooks.size()));
                    }
                } finally {
                    completionLatch.countDown();
                }
            });
        }

        try {
            if (!completionLatch.await(5, TimeUnit.MINUTES)) {
                System.out.println("Warning: Timed out waiting for recommendations");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Recommendation calculation interrupted");
        }

        System.out.printf("Calculation completed in %d ms%n",
            System.currentTimeMillis() - startTime);

        TreeMap<Book, Double> resultMap = new TreeMap<>(
            Comparator.<Book, Double>comparing(similarityScores::get).reversed()
                .thenComparing(Book::title)
        );

        similarityScores.entrySet().stream()
            .limit(maxN)
            .forEach(entry -> resultMap.put(entry.getKey(), entry.getValue()));

        return resultMap;
    }

    public void shutdown() {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Thread pool did not terminate");
                }
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}



