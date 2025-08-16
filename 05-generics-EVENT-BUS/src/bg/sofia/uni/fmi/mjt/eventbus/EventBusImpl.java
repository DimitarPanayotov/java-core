package bg.sofia.uni.fmi.mjt.eventbus;

import bg.sofia.uni.fmi.mjt.eventbus.events.Event;
import bg.sofia.uni.fmi.mjt.eventbus.subscribers.Subscriber;
import bg.sofia.uni.fmi.mjt.eventbus.exception.MissingSubscriptionException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBusImpl implements EventBus {
    private final Map<Class<? extends Event<?>>, List<Subscriber<?>>> subscribers;
    private final Map<Class<? extends Event<?>>, List<Event<?>>> eventLogs;

    public EventBusImpl() {
        this.subscribers = new HashMap<>();
        this.eventLogs = new HashMap<>();
    }

    @Override
    public <T extends Event<?>> void subscribe(Class<T> eventType, Subscriber<? super T> subscriber) {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type and subscriber cannot be null!");
        }

        if (!subscribers.containsKey(eventType)) {
            subscribers.put(eventType, new ArrayList<>());
        }
        subscribers.get(eventType).add(subscriber);
    }

    @Override
    public <T extends Event<?>> void unsubscribe(Class<T> eventType, Subscriber<? super T> subscriber)
        throws MissingSubscriptionException {
        if (eventType == null || subscriber == null) {
            throw new IllegalArgumentException("Event type or subscriber is null");
        }

        List<Subscriber<?>> subs = subscribers.get(eventType);
        if (subs == null || !subs.remove(subscriber)) {
            throw new MissingSubscriptionException("Subscriber is not registered for this event type");
        }

        if (subs.isEmpty()) {
            subscribers.remove(eventType);
        }
    }

    @Override
    public <T extends Event<?>> void publish(T event) {
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }

        Class<? extends Event<?>> eventType = (Class<? extends Event<?>>) event.getClass();

        if (!eventLogs.containsKey(eventType)) {
            eventLogs.put(eventType, new ArrayList<>());
        }
        eventLogs.get(eventType).add(event);

        List<Subscriber<?>> subs = subscribers.get(eventType);
        if (subs != null) {
            for (Subscriber<?> subscriber : subs) {
                Subscriber<? super T> typedSubscriber = (Subscriber<? super T>) subscriber;
                typedSubscriber.onEvent(event);
            }
        }
    }

    @Override
    public void clear() {
        subscribers.clear();
        eventLogs.clear();
    }

    @Override
    public Collection<? extends Event<?>> getEventLogs(Class<? extends Event<?>> eventType, Instant from, Instant to) {
        if (eventType == null || from == null || to == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (!eventLogs.containsKey(eventType)) {
            return Collections.emptyList();
        }

        List<Event<?>> logs = eventLogs.getOrDefault(eventType, Collections.emptyList());
        List<Event<?>> result = new ArrayList<>();

        for (Event<?> e : logs) {
            if (!e.getTimestamp().isBefore(from) && e.getTimestamp().isBefore(to)) {
                result.add(e);
            }
        }

        return Collections.unmodifiableCollection(result);
    }

    @Override
    public <T extends Event<?>> Collection<Subscriber<?>> getSubscribersForEvent(Class<T> eventType) {
        if (eventType == null) {
            throw new IllegalArgumentException("Event type cannot be null");
        }

        List<Subscriber<?>> subs = subscribers.getOrDefault(eventType, Collections.emptyList());

        return Collections.unmodifiableCollection(subs);
    }
}
