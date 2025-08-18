package bg.sofia.uni.fmi.mjt.newsfeed.exception;

public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
