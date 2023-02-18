package com.bakuard.pomodoro.event;

@FunctionalInterface
public interface EventListener {

    public void listen(Event event);

}
