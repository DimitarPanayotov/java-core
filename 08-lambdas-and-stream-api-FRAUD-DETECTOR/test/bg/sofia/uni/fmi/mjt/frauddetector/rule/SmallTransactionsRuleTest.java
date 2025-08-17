package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SmallTransactionsRuleTest {

    private static final double DELTA = 1e-6;

    private Transaction createTransaction(double amount) {
        return new Transaction("tx", "acc", amount, LocalDateTime.now(), "Sofia", Channel.ATM);
    }

    @Test
    public void testApplicableWhenSmallTransactionsExceedThreshold() {
        List<Transaction> transactions = List.of(
            createTransaction(1.0),
            createTransaction(2.0),
            createTransaction(0.5),
            createTransaction(5.0)
        );

        Rule rule = new SmallTransactionsRule(3, 5.0, 0.4);
        assertTrue(rule.applicable(transactions), "Rule should be applicable when small transactions ≥ threshold");
    }

    @Test
    public void testNotApplicableWhenSmallTransactionsBelowThreshold() {
        List<Transaction> transactions = List.of(
            createTransaction(1.0),
            createTransaction(4.0),
            createTransaction(5.0)
        );

        Rule rule = new SmallTransactionsRule(3, 5.0, 0.4);
        assertFalse(rule.applicable(transactions), "Rule should not be applicable when small transactions < threshold");
    }

    @Test
    public void testApplicableWhenExactlyAtThreshold() {
        List<Transaction> transactions = List.of(
            createTransaction(1.0),
            createTransaction(2.0),
            createTransaction(3.0)
        );

        Rule rule = new SmallTransactionsRule(3, 5.0, 0.4);
        assertTrue(rule.applicable(transactions), "Rule should be applicable with exact threshold match");
    }

    @Test
    public void testWeightReturnsCorrectValue() {
        Rule rule = new SmallTransactionsRule(2, 5.0, 0.33);
        assertEquals(0.33, rule.weight(), DELTA, "Weight should match the value passed to constructor");
    }

    @Test
    public void testNotApplicableWithEmptyList() {
        Rule rule = new SmallTransactionsRule(1, 10.0, 0.1);
        assertFalse(rule.applicable(List.of()), "Rule should not be applicable with empty list");
    }
}
