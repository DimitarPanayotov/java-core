package bg.sofia.uni.fmi.mjt.eventbus.events;

public class SystemPayload implements Payload<Integer> {
    private final int statusCode;
    private final String message;

    public SystemPayload(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public int getSize() {
        return message.length();
    }

    @Override
    public Integer getPayload() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}