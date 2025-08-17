package bg.sofia.uni.fmi.mjt.frauddetector.rule;


import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FrequencyRuleTest {

    private static Transaction createTransaction(LocalDateTime date) {
        return new Transaction("TX", "ACC", 10.0, date, "Sofia", Channel.ATM);
    }

    @Test
    public void testApplicableWhenTransactionCountExceedsThreshold() {
        LocalDateTime now = LocalDateTime.now();

        List<Transaction> transactions = List.of(
            createTransaction(now.minusDays(1)),
            createTransaction(now.minusDays(2)),
            createTransaction(now.minusDays(3))
        );

        Rule rule = new FrequencyRule(3, Duration.ofDays(7), 0.5);
        assertTrue(rule.applicable(transactions), "Rule should be applicable when threshold is met");
    }

    @Test
    public void testNotApplicableWhenTransactionCountBelowThreshold() {
        LocalDateTime now = LocalDateTime.now();

        List<Transaction> transactions = List.of(
            createTransaction(now.minusDays(2)),
            createTransaction(now.minusDays(5))
        );

        Rule rule = new FrequencyRule(3, Duration.ofDays(7), 0.5);
        assertFalse(rule.applicable(transactions), "Rule should not be applicable when below threshold");
    }

    @Test
    public void testNotApplicableWhenTransactionsAreOutsideWindow() {
        LocalDateTime now = LocalDateTime.now();

        List<Transaction> transactions = List.of(
            createTransaction(LocalDateTime.now()),
            createTransaction(now.minusDays(30)),
            createTransaction(now.minusDays(25)),
            createTransaction(now.minusDays(20))
        );

        Rule rule = new FrequencyRule(2, Duration.ofDays(7), 0.3);
        assertFalse(rule.applicable(transactions), "Rule should not be applicable when all are outside window");
    }

    @Test
    public void testNotApplicableWhenListIsEmpty() {
        Rule rule = new FrequencyRule(1, Duration.ofDays(1), 0.1);
        assertFalse(rule.applicable(List.of()), "Empty transaction list should return false");
    }

    @Test
    public void testNotApplicableWhenListIsNull() {
        Rule rule = new FrequencyRule(1, Duration.ofDays(1), 0.1);
        assertFalse(rule.applicable(null), "Null transaction list should return false");
    }

    @Test
    public void testCorrectWeightIsReturned() {
        Rule rule = new FrequencyRule(1, Duration.ofHours(1), 0.77);
        assertEquals(0.77, rule.weight(), 0.0001, "Weight should match the one set in constructor");
    }

    @Test
    public void testConstructorThrowsForNegativeThreshold() {
        assertThrows(IllegalArgumentException.class, () -> new FrequencyRule(-1, Duration.ofMinutes(30), 0.5));
    }

    @Test
    public void testConstructorThrowsForInvalidWeight() {
        assertThrows(IllegalArgumentException.class, () -> new FrequencyRule(1, Duration.ofMinutes(30), 1.5));
        assertThrows(IllegalArgumentException.class, () -> new FrequencyRule(1, Duration.ofMinutes(30), -0.1));
    }
}
