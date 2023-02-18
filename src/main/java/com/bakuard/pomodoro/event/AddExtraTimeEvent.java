package com.bakuard.pomodoro.event;

import com.bakuard.pomodoro.controller.CounterController;

public record AddExtraTimeEvent(CounterController counterController, int hours, int minutes, int seconds) implements Event {}
