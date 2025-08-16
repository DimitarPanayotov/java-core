package bg.sofia.uni.fmi.mjt.eventbus.exception;

public class MissingSubscriptionException extends Exception {
    public MissingSubscriptionException(String message) {
        super(message);
    }
}
