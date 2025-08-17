package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class SmallTransactionsRule implements Rule {
    private int countThreshold;
    private double amountThreshold;
    double weight;

    public SmallTransactionsRule(int countThreshold, double amountThreshold, double weight) {
        if (countThreshold < 0) {
            throw new IllegalArgumentException("Count threshold cannot be negative!");
        }
        if (amountThreshold < 0) {
            throw new IllegalArgumentException("Amount threshold cannot be negative!");
        }
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be in [0.0, 1.0]");
        }

        this.countThreshold = countThreshold;
        this.amountThreshold = amountThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return false;
        }

        long count = transactions.stream()
            .filter(t -> t.transactionAmount() < amountThreshold)
            .count();

        return count >= countThreshold;
    }

    @Override
    public double weight() {
        return weight;
    }

}
