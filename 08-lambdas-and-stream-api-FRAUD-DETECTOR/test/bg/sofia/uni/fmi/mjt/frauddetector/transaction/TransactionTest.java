package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTest {
    @Test
    public void testTransactionParsing () {
        List<String> lines = List.of(
            "TX000001,AC00128,14.09,2023-04-11 16:29:14,San Diego,ATM",
            "TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM",
            "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online",
            "TX000004,AC00070,184.5,2023-05-05 16:32:11,Raleigh,Online",
            "TX000005,AC00411,13.45,2023-10-16 17:51:24,Atlanta,Online",
            "TX000006,AC00393,92.15,2023-04-03 17:15:01,Oklahoma City,ATM",
            "TX000007,AC00199,7.08,2023-02-15 16:36:48,Seattle,ATM",
            "TX000008,AC00069,171.42,2023-05-08 17:47:59,Indianapolis,Branch",
            "TX000009,AC00135,106.23,2023-03-21 16:59:46,Detroit,Branch",
            "TX000010,AC00385,815.96,2023-03-31 16:06:57,Nashville,ATM"
        );

        Transaction tx1 = Transaction.of(lines.get(0));
        assertEquals("TX000001", tx1.transactionID());
        assertEquals("AC00128", tx1.accountID());
        assertEquals(14.09, tx1.transactionAmount(), 0.001);
        assertEquals(LocalDateTime.of(2023, 4, 11, 16, 29, 14), tx1.transactionDate());
        assertEquals("San Diego", tx1.location());
        assertEquals(Channel.ATM, tx1.channel());

        Transaction tx3 = Transaction.of(lines.get(2));
        assertEquals("TX000003", tx3.transactionID());
        assertEquals("Mesa", tx3.location());
        assertEquals(Channel.ONLINE, tx3.channel());

        Transaction tx10 = Transaction.of(lines.get(9));
        assertEquals("TX000010", tx10.transactionID());
        assertEquals("Nashville", tx10.location());
        assertEquals(Channel.ATM, tx10.channel());
    }
}
