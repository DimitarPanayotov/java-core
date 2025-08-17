package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationsRule implements Rule {
    private int threshold;
    private double weight;

    public LocationsRule(int threshold, double weight) {
        if (threshold < 0) {
            throw new IllegalArgumentException("Transaction count threshold must be non-negative.");
        }
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be in [0.0, 1.0]");
        }
        this.threshold = threshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return false;
        }
        Set<String> uniqueLocations = transactions.stream()
            .map(Transaction::location)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        return uniqueLocations.size() >= threshold;
    }

    @Override
    public double weight() {
        return weight;
    }
}
