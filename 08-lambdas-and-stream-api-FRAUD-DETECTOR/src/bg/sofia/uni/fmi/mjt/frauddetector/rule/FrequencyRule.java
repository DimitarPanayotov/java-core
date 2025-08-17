package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public class FrequencyRule implements Rule {
    private int transactionCountThreshold;
    private TemporalAmount timeWindow;
    private double weight;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        if (transactionCountThreshold < 0) {
            throw new IllegalArgumentException("Transaction count threshold must be non-negative.");
        }
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be in [0.0, 1.0]");
        }
        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return false;
        }
        LocalDateTime referenceTime = transactions.stream()
            .map(Transaction::transactionDate)
            .max(LocalDateTime::compareTo)
            .orElseThrow();
        LocalDateTime windowStart = referenceTime.minus(timeWindow);

        long count = transactions.stream()
            .filter(tx -> {
                LocalDateTime date = tx.transactionDate();
                return !date.isBefore(windowStart) && !date.isAfter(referenceTime);
            })
            .count();

        return count >= transactionCountThreshold;
    }

    @Override
    public double weight() {
        return weight;
    }

}
