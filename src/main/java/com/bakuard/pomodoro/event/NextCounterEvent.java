package com.bakuard.pomodoro.event;

import com.bakuard.pomodoro.model.counter.Counter;

public record NextCounterEvent(Counter recentActiveCounter) implements Event {}
