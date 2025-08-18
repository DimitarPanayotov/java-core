package bg.sofia.uni.fmi.mjt.newsfeed.exception;

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(message);
    }
}
