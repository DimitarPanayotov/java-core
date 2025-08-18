package bg.sofia.uni.fmi.mjt.newsfeed.exception;

public class ServerErrorException extends ApiException {
    public ServerErrorException(String message) {
        super(message);
    }
}
