package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ZScoreRuleTest {

    private Transaction createTransaction(double amount) {
        return new Transaction("TX", "AC", amount, LocalDateTime.now(), "Sofia", Channel.ATM);
    }

    @Test
    public void testApplicableWhenOneTransactionExceedsZScoreThreshold() {
        Rule rule = new ZScoreRule(1.5, 0.5);

        List<Transaction> transactions = List.of(
            createTransaction(10.0),
            createTransaction(12.0),
            createTransaction(12.0),
            createTransaction(10.0),
            createTransaction(8.0),
            createTransaction(1000.0)
        );

        assertTrue(rule.applicable(transactions), "Rule should be applicable due to one large outlier");
    }

    @Test
    public void testNotApplicableWhenAllTransactionsAreCloseToAverage() {
        Rule rule = new ZScoreRule(2.0, 0.5);

        List<Transaction> transactions = List.of(
            createTransaction(10.0),
            createTransaction(11.0),
            createTransaction(9.0)
        );

        assertFalse(rule.applicable(transactions), "Rule should not be applicable since all transactions are close");
    }

    @Test
    public void testNotApplicableWhenStdDeviationIsZero() {
        Rule rule = new ZScoreRule(1.0, 0.5);

        List<Transaction> transactions = List.of(
            createTransaction(15.0),
            createTransaction(15.0),
            createTransaction(15.0)
        );

        assertFalse(rule.applicable(transactions), "Rule should not be applicable with zero standard deviation");
    }

    @Test
    public void testNotApplicableWhenTransactionListIsEmpty() {
        Rule rule = new ZScoreRule(1.0, 0.5);

        List<Transaction> transactions = List.of();

        assertFalse(rule.applicable(transactions), "Rule should not be applicable for empty list");
    }

    @Test
    public void testNotApplicableWhenTransactionListIsNull() {
        Rule rule = new ZScoreRule(1.0, 0.5);

        assertFalse(rule.applicable(null), "Rule should not be applicable for null list");
    }

    @Test
    public void testApplicableWhenZScoreEqualsThreshold() {
        Rule rule = new ZScoreRule(1.0, 0.5);

        List<Transaction> transactions = List.of(
            createTransaction(9.0),
            createTransaction(10.0),
            createTransaction(11.0)
        );

        assertTrue(rule.applicable(transactions), "Rule should be applicable when Z-score equals threshold");
    }

    @Test
    public void testWeightGetter() {
        Rule rule = new ZScoreRule(1.0, 0.75);
        assertEquals(0.75, rule.weight(), 0.00001, "Weight getter should return correct value");
    }
}

