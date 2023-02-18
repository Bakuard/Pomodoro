package com.bakuard.pomodoro.event;

import com.bakuard.pomodoro.controller.CounterController;

public record SubtractTimeEvent(CounterController counterController, int hours, int minutes, int seconds) implements Event {}
