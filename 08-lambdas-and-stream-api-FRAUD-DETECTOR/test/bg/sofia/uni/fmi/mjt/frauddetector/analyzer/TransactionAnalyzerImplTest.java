package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.ZScoreRule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionAnalyzerImplTest {

    private static final String TRANSACTIONS_CSV = """
        transactionID,accountID,amount,transactionDate,location,channel
        t1,user1,100.0,2023-01-01 10:00:00,Sofia,ONLINE
        t2,user1,200.0,2023-01-01 10:05:00,Plovdiv,ONLINE
        t3,user2,50.0,2023-01-02 11:00:00,Varna,BRANCH
        t4,user3,300.0,2023-01-03 12:00:00,Sofia,ATM
        t5,user3,150.0,2023-01-03 12:30:00,Plovdiv,ONLINE
        t6,user1,500.0,2023-01-01 10:10:00,Sofia,ONLINE
        t7,user4,10.0,2023-01-04 09:00:00,Burgas,ATM
        t8,user4,10.0,2023-01-04 09:05:00,Burgas,ATM
        t9,user4,10.0,2023-01-04 09:10:00,Burgas,ATM
        """;

    private TransactionAnalyzer analyzer;
    private List<Rule> rules;

    @BeforeEach
    void setUp() throws Exception {
        rules = List.of(
            new FrequencyRule(3, Duration.ofHours(1), 0.3),
            new LocationsRule(2, 0.2),
            new SmallTransactionsRule(3, 20.0, 0.3),
            new ZScoreRule(2.5, 0.2)
        );

        StringReader reader = new StringReader(TRANSACTIONS_CSV);
        analyzer = new TransactionAnalyzerImpl(reader, rules);
    }

    @Test
    void testConstructorWithNullReader() {
        assertThrows(IllegalArgumentException.class,
            () -> new TransactionAnalyzerImpl(null, rules),
            "Should throw when reader is null");
    }

    @Test
    void testConstructorWithNullRules() {
        assertThrows(IllegalArgumentException.class,
            () -> new TransactionAnalyzerImpl(new StringReader(TRANSACTIONS_CSV), null),
            "Should throw when rules is null");
    }

    @Test
    void testConstructorWithInvalidRuleWeights() {
        List<Rule> invalidRules = List.of(
            new FrequencyRule(3, Duration.ofHours(1), 0.6),
            new LocationsRule(2, 0.5)
        );

        assertThrows(IllegalArgumentException.class,
            () -> new TransactionAnalyzerImpl(new StringReader(TRANSACTIONS_CSV), invalidRules),
            "Should throw when rule weights sum > 1.0");
    }

    @Test
    void testAllTransactions() {
        List<Transaction> transactions = analyzer.allTransactions();

        assertEquals(9, transactions.size(), "Should return all transactions");
        assertEquals("t1", transactions.get(0).transactionID(), "First transaction should be t1");
        assertEquals("t9", transactions.get(8).transactionID(), "Last transaction should be t9");
    }

    @Test
    void testAllAccountIDs() {
        List<String> accountIDs = analyzer.allAccountIDs();

        assertEquals(4, accountIDs.size(), "Should return all unique account IDs");
        assertTrue(accountIDs.containsAll(List.of("user1", "user2", "user3", "user4")),
            "Should contain all test account IDs");
    }

    @Test
    void testTransactionCountByChannel() {
        Map<Channel, Integer> counts = analyzer.transactionCountByChannel();

        assertEquals(3, counts.size(), "Should have counts for 3 channels");
        assertEquals(4, counts.get(Channel.ONLINE), "Should have 5 ONLINE transactions");
        assertEquals(1, counts.get(Channel.BRANCH), "Should have 1 BRANCH transaction");
        assertEquals(4, counts.get(Channel.ATM), "Should have 3 ATM transactions");
    }

    @Test
    void testAmountSpentByUser() {
        assertEquals(800.0, analyzer.amountSpentByUser("user1"), 0.001,
            "User1 should have spent 800 total");
        assertEquals(50.0, analyzer.amountSpentByUser("user2"), 0.001,
            "User2 should have spent 50 total");
        assertEquals(450.0, analyzer.amountSpentByUser("user3"), 0.001,
            "User3 should have spent 450 total");
    }

    @Test
    void testAmountSpentByUserWithInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(null),
            "Should throw when accountID is null");
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(""),
            "Should throw when accountID is empty");
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser("   "),
            "Should throw when accountID is blank");
    }

    @Test
    void testAllTransactionsByUser() {
        List<Transaction> user1Transactions = analyzer.allTransactionsByUser("user1");
        List<Transaction> user4Transactions = analyzer.allTransactionsByUser("user4");

        assertEquals(3, user1Transactions.size(), "User1 should have 3 transactions");
        assertEquals(3, user4Transactions.size(), "User4 should have 3 transactions");
        assertEquals("t1", user1Transactions.get(0).transactionID(), "First transaction of user1 should be t1");
    }

    @Test
    void testAllTransactionsByUserWithInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.allTransactionsByUser(null),
            "Should throw when accountId is null");
        assertThrows(IllegalArgumentException.class, () -> analyzer.allTransactionsByUser(""),
            "Should throw when accountId is empty");
        assertThrows(IllegalArgumentException.class, () -> analyzer.allTransactionsByUser("   "),
            "Should throw when accountId is blank");
    }

    @Test
    void testAccountRating() {
        assertEquals(0.6, analyzer.accountRating("user4"), 0.001,
            "User4 should have rating 0.6 (0.3 + 0.3)");

        assertEquals(0.5, analyzer.accountRating("user1"), 0.001,
            "User1 should have rating 0.2 (only LocationsRule)");

        assertEquals(0.0, analyzer.accountRating("user2"), 0.001,
            "User2 should have rating 0.0 (no rules matched)");
    }

    @Test
    void testAccountRatingWithInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.accountRating(null),
            "Should throw when accountId is null");
        assertThrows(IllegalArgumentException.class, () -> analyzer.accountRating(""),
            "Should throw when accountId is empty");
        assertThrows(IllegalArgumentException.class, () -> analyzer.accountRating("   "),
            "Should throw when accountId is blank");
    }

    @Test
    void testAccountsRisk() {
        Map<String, Double> riskMap = analyzer.accountsRisk();

        assertEquals(4, riskMap.size(), "Should have risk scores for 4 users");

        String[] expectedOrder = {"user4", "user1", "user3", "user2"};
        assertArrayEquals(expectedOrder, riskMap.keySet().toArray(),
            "Accounts should be ordered by descending risk");

        assertEquals(0.6, riskMap.get("user4"), 0.001, "User4 should have highest risk");
        assertEquals(0.5, riskMap.get("user1"), 0.001, "User1 should have second highest risk");
        assertEquals(0.0, riskMap.get("user2"), 0.001, "User2 should have lowest risk");
    }

    @Test
    void testAccountsRiskWithEmptyTransactions() throws Exception {
        StringReader reader = new StringReader("transactionID,accountID,amount,transactionDate,location,channel");
        TransactionAnalyzer emptyAnalyzer = new TransactionAnalyzerImpl(reader, rules);

        Map<String, Double> riskMap = emptyAnalyzer.accountsRisk();
        assertTrue(riskMap.isEmpty(), "Should return empty map for no transactions");
    }
}
