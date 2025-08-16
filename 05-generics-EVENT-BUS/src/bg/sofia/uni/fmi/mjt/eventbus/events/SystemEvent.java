package bg.sofia.uni.fmi.mjt.eventbus.events;

import java.time.Instant;

public class SystemEvent implements Event<SystemPayload> {
    private final Instant timestamp;
    private final int priority;
    private final String source;
    private final SystemPayload payload;

    public SystemEvent(int priority, String source, SystemPayload payload) {
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
    public SystemPayload getPayload() {
        return payload;
    }
}