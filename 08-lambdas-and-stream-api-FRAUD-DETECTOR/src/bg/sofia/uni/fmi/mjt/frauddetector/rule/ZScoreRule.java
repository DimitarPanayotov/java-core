package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule implements Rule  {
    private double zScoreThreshold;
    private double weight;

    public ZScoreRule(double zScoreThreshold, double weight) {
        if (zScoreThreshold < 0) {
            throw new IllegalArgumentException("ZScore threshold cannot be negative!");
        }
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be in [0.0, 1.0]");
        }
        this.zScoreThreshold = zScoreThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return false;
        }

        double averageTransactionAmount = transactions.stream()
            .mapToDouble(Transaction::transactionAmount)
            .average()
            .orElse(0.0);
        double variance = transactions.stream()
            .mapToDouble(Transaction::transactionAmount)
            .map(x -> Math.pow(x - averageTransactionAmount, 2))
            .average()
            .orElse(0.0);

        double standardDeviation = Math.sqrt(variance);
        if (standardDeviation == 0.0) {
            return false;
        }

        return transactions.stream()
            .mapToDouble(Transaction::transactionAmount)
            .anyMatch(amount -> Math.abs((amount - averageTransactionAmount) / standardDeviation) >= zScoreThreshold);

    }

    @Override
    public double weight() {
        return weight;
    }

}
