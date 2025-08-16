package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import bg.sofia.uni.fmi.mjt.eventbus.events.SimpleEvent;

public class SimpleEventSubscriber implements Subscriber<SimpleEvent> {
    private final String name;

    public SimpleEventSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(SimpleEvent event) {
        System.out.printf("[%s] Received SimpleEvent: Priority=%d, Source='%s', Payload='%s'%n",
            name, event.getPriority(), event.getSource(), event.getPayload().getPayload());
    }
}
