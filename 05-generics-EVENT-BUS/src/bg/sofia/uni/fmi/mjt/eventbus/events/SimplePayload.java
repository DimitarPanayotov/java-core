package bg.sofia.uni.fmi.mjt.eventbus.events;

public class SimplePayload implements Payload<String> {
    private final String payload;

    public SimplePayload(String payload) {
        this.payload = payload;
    }

    @Override
    public int getSize() {
        return payload.length();
    }

    @Override
    public String getPayload() {
        return payload;
    }
}