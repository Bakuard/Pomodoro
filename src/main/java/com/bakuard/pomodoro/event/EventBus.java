package com.bakuard.pomodoro.event;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class EventBus {

    private final Map<Class<?>, List<EventListener>> subscribers;

    public EventBus() {
        subscribers = new ConcurrentHashMap<>();
    }

    public void register(Class<? extends Event> eventType, EventListener subscriber) {
        Objects.requireNonNull(eventType, "eventType can't be null.");
        Objects.requireNonNull(subscriber, "subscriber can't be null.");

        List<EventListener> eventSubscribers = getOrCreateSubscribers(eventType);
        eventSubscribers.add(subscriber);
    }

    public void unregister(EventListener subscriber) {
        Objects.requireNonNull(subscriber, "subscriber can't be null.");

        subscribers.values().forEach(eventSubscribers -> eventSubscribers.remove(subscriber));
    }

    public void unregister(Class<? extends Event> eventType, EventListener subscriber) {
        Objects.requireNonNull(eventType, "eventType can't be null.");
        Objects.requireNonNull(subscriber, "subscriber can't be null.");

        subscribers.keySet().stream()
                .filter(eventType::isAssignableFrom)
                .map(subscribers::get)
                .forEach(eventSubscribers -> eventSubscribers.remove(subscriber));
    }

    public void post(Event event) {
        Objects.requireNonNull(event, "event can't be null.");

        Class<?> eventType = event.getClass();
        subscribers.keySet().stream()
                .filter(type -> type.isAssignableFrom(eventType))
                .flatMap(type -> subscribers.get(type).stream())
                .forEach(subscriber -> subscriber.listen(event));
    }


    private <T> List<EventListener> getOrCreateSubscribers(Class<T> eventType) {
        List<EventListener> eventSubscribers = subscribers.get(eventType);
        if (eventSubscribers == null) {
            eventSubscribers = new CopyOnWriteArrayList<>();
            subscribers.put(eventType, eventSubscribers);
        }
        return eventSubscribers;
    }

}
