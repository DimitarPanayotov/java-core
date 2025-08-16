package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.events.SimpleEvent;
import bg.sofia.uni.fmi.mjt.eventbus.events.SimplePayload;
import bg.sofia.uni.fmi.mjt.eventbus.events.SystemEvent;
import bg.sofia.uni.fmi.mjt.eventbus.events.SystemPayload;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.DeferredEventSubscriber;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.SimpleEventSubscriber;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.SystemEventSubscriber;

import java.time.Instant;
import java.util.Collection;

public class Main {
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:MagicNumber"})
    public static void main(String[] args) throws InterruptedException {
        // Create the event bus
        EventBus eventBus = new EventBusImpl();

        // Create subscribers
        SimpleEventSubscriber simpleSub1 = new SimpleEventSubscriber("SimpleSub1");
        SimpleEventSubscriber simpleSub2 = new SimpleEventSubscriber("SimpleSub2");
        SystemEventSubscriber systemSub1 = new SystemEventSubscriber("SystemSub1");

        // Create a deferred subscriber
        DeferredEventSubscriber<SimpleEvent> deferredSub = new DeferredEventSubscriber<>();

        // Subscribe subscribers
        eventBus.subscribe(SimpleEvent.class, simpleSub1);
        eventBus.subscribe(SimpleEvent.class, simpleSub2);
        eventBus.subscribe(SystemEvent.class, systemSub1);
        eventBus.subscribe(SimpleEvent.class, deferredSub);

        // Create and publish some events
        SimplePayload payload1 = new SimplePayload("Hello World!");
        SimpleEvent event1 = new SimpleEvent(1, "Main", payload1);
        eventBus.publish(event1);

        SystemPayload systemPayload1 = new SystemPayload(200, "System is OK");
        SystemEvent systemEvent1 = new SystemEvent(2, "SystemMonitor", systemPayload1);
        eventBus.publish(systemEvent1);

        SimplePayload payload2 = new SimplePayload("Another message");
        SimpleEvent event2 = new SimpleEvent(3, "Main", payload2);
        eventBus.publish(event2);

        // Wait a bit to see timestamps change
        Thread.sleep(100);

        SimplePayload payload3 = new SimplePayload("Priority test");
        SimpleEvent event3 = new SimpleEvent(1, "Main", payload3); // Higher priority
        eventBus.publish(event3);

        // Test deferred subscriber
        System.out.println("\nProcessing deferred events:");
        for (SimpleEvent event : deferredSub) {
            System.out.printf("Deferred event: Priority=%d, Payload='%s'%n",
                event.getPriority(), event.getPayload().getPayload());
        }

        // Test event logs
        System.out.println("\nEvent logs for SimpleEvent:");
        Collection<? extends Event<?>> logs = eventBus.getEventLogs(
            SimpleEvent.class, Instant.now().minusSeconds(10), Instant.now());
        for (Event<?> event : logs) {
            SimpleEvent se = (SimpleEvent) event;
            System.out.printf("Logged SimpleEvent: Priority=%d, Payload='%s'%n",
                se.getPriority(), se.getPayload().getPayload());
        }

        // Test unsubscribe
        try {
            eventBus.unsubscribe(SimpleEvent.class, simpleSub1);
            System.out.println("\nUnsubscribed simpleSub1 successfully");
        } catch (Exception e) {
            System.out.println("Failed to unsubscribe: " + e.getMessage());
        }

        // Test missing subscription
        SimpleEventSubscriber neverSubscribed = new SimpleEventSubscriber("NeverSubscribed");

        try {
            eventBus.unsubscribe(SimpleEvent.class, neverSubscribed);
        } catch (Exception e) {
            System.out.println("\nExpected error when unsubscribing: " + e.getMessage());
        }

        // Test clear
        eventBus.clear();
        System.out.println("\nEvent bus cleared. Current subscribers for SimpleEvent: " +
            eventBus.getSubscribersForEvent(SimpleEvent.class).size());
    }
}



