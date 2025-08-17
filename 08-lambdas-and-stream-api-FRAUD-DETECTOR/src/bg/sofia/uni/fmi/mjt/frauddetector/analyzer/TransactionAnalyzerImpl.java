package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {
    private static final double EPSILON = 1e-6;
    private final List<Transaction> transactions;
    private final List<Rule> rules;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) throws IOException {
        if (reader == null || rules == null) {
            throw new IllegalArgumentException("Reader and rules must not be null.");
        }

        this.rules = List.copyOf(rules);

        double totalRulesWeights = rules.stream()
            .mapToDouble(Rule::weight)
            .sum();
        if (Math.abs(totalRulesWeights - 1.0) > EPSILON) {
            throw new IllegalArgumentException("Total rule weights must sum up to 1.0 (±ε). Found: "
                + totalRulesWeights);
        }

        var buff = new BufferedReader(reader);
        buff.readLine();
        transactions = buff.lines().map(Transaction::of).toList();
    }

    @Override
    public List<Transaction> allTransactions() {
        return List.copyOf(transactions);
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream()
            .map(Transaction::accountID)
            .distinct()
            .toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream()
            .collect(Collectors.groupingBy(
                Transaction::channel,
                Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
            ));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null || accountID.isBlank()) {
            throw new IllegalArgumentException("accountID cannot be null or empty");
        }

        return transactions.stream()
            .filter(tx -> tx.accountID().equals(accountID))
            .mapToDouble(Transaction::transactionAmount)
            .sum();
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        return transactions.stream()
            .filter(tx -> tx.accountID().equals(accountId))
            .toList();
    }

    @Override
    public double accountRating(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
        List<Transaction> userTransactions = allTransactionsByUser(accountId);

        return rules.stream()
            .filter(rule -> rule.applicable(userTransactions))
            .mapToDouble(Rule::weight)
            .sum();
    }

    @Override
    public Map<String, Double> accountsRisk() {
        Map<String, Double> riskByAccount = transactions.stream()
            .collect(Collectors.groupingBy(
                Transaction::accountID,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    userTransactions -> rules.stream()
                        .filter(rule -> rule.applicable(userTransactions))
                        .mapToDouble(Rule::weight)
                        .sum()
                )
            ));

        return riskByAccount.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }

}
