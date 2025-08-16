package bg.sofia.uni.fmi.mjt.eventbus.subscribers;

import bg.sofia.uni.fmi.mjt.eventbus.events.SystemEvent;

public class SystemEventSubscriber implements Subscriber<SystemEvent> {
    private final String name;

    public SystemEventSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onEvent(SystemEvent event) {
        System.out.printf("[%s] Received SystemEvent: Priority=%d, Source='%s', Status=%d, Message='%s'%n",
            name, event.getPriority(), event.getSource(),
            event.getPayload().getPayload(), event.getPayload().getMessage());
    }
}