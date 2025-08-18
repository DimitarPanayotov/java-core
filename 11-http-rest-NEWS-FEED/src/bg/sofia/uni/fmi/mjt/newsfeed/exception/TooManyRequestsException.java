package bg.sofia.uni.fmi.mjt.newsfeed.exception;

public class TooManyRequestsException extends ApiException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}
