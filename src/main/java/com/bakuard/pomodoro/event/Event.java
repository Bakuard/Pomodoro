package com.bakuard.pomodoro.event;

public interface Event {

    public default String getName() {
        return getClass().getSimpleName();
    }

}
