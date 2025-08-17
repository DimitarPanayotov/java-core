package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID, String accountID,
                          double transactionAmount, LocalDateTime transactionDate,
                          String location, Channel channel) {
    public static final String TRANSACTION_CSV_SEPARATOR = ",";
    public static final DateTimeFormatter TRANSACTION_DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("checkstyle:MagicNumber")
    public static Transaction of(String line) {
        final String[] tokens = line.split(TRANSACTION_CSV_SEPARATOR);
        String transactionId = tokens[0];
        String accountId = tokens[1];
        double transactionAmount = Double.parseDouble(tokens[2]);
        LocalDateTime transactionDate = LocalDateTime.parse(tokens[3], TRANSACTION_DATE_FORMATTER);
        String location = tokens[4];
        Channel channel = Channel.valueOf(tokens[5].toUpperCase());
        return new Transaction(transactionId, accountId, transactionAmount,
            transactionDate, location, channel);
    }

}
