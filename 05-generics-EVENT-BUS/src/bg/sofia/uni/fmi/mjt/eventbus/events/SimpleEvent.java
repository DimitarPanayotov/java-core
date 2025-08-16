package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.time.Instant;

public class SimpleEvent implements Event<SimplePayload> {
    private final Instant timestamp;
    private final int priority;
    private final String source;
    private final SimplePayload payload;

    public SimpleEvent(int priority, String source, SimplePayload payload) {
        this.timestamp = Instant.now();
        this.priority = priority;
        this.source = source;
        this.payload = payload;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public String getSource() {
        return source;
    }

    @Override
    public SimplePayload getPayload() {
        return payload;
    }
}
