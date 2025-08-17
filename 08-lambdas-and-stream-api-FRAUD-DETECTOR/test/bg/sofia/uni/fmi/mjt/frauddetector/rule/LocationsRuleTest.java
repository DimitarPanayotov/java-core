package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LocationsRuleTest {

    private Transaction createTransaction(String location) {
        return new Transaction("id", "acc", 0.0, LocalDateTime.now(), location, Channel.ATM);
    }

    @Test
    public void testApplicableWhenUniqueLocationsAboveThreshold() {
        List<Transaction> transactions = List.of(
            createTransaction("Sofia"),
            createTransaction("Plovdiv"),
            createTransaction("Varna"),
            createTransaction("Sofia") // Duplicate
        );

        Rule rule = new LocationsRule(3, 0.5);
        assertTrue(rule.applicable(transactions), "Rule should be applicable with 3 unique locations");
    }

    @Test
    public void testNotApplicableWhenUniqueLocationsBelowThreshold() {
        List<Transaction> transactions = List.of(
            createTransaction("Sofia"),
            createTransaction("Plovdiv"),
            createTransaction("Sofia")
        );

        Rule rule = new LocationsRule(3, 0.5);
        assertFalse(rule.applicable(transactions), "Rule should NOT be applicable with only 2 unique locations");
    }

    @Test
    public void testApplicableWithExactThreshold() {
        List<Transaction> transactions = List.of(
            createTransaction("Sofia"),
            createTransaction("Plovdiv"),
            createTransaction("Varna")
        );

        Rule rule = new LocationsRule(3, 0.5);
        assertTrue(rule.applicable(transactions), "Rule should be applicable with exactly 3 unique locations");
    }

    @Test
    public void testNotApplicableWithEmptyList() {
        Rule rule = new LocationsRule(1, 0.5);
        assertFalse(rule.applicable(List.of()), "Rule should not be applicable with empty transactions list");
    }
}
