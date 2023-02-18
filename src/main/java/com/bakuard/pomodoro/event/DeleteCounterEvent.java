package com.bakuard.pomodoro.event;

import com.bakuard.pomodoro.model.counter.Counter;

public record DeleteCounterEvent(Counter counter) implements Event {}
