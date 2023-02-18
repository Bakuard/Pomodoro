package com.bakuard.pomodoro.model.counter;

import com.bakuard.pomodoro.model.exception.IncorrectCounterNameException;
import com.bakuard.pomodoro.model.exception.IncorrectTimeException;

import java.util.Objects;
import java.util.UUID;

public class Counter {

    private final UUID id;
    private String name;
    private long currentTotalSeconds;
    private long initTotalSeconds;

    public Counter(UUID id, String name, long currentTotalSeconds, long initTotalSeconds) {
        this.id = id;
        this.name = name;
        this.currentTotalSeconds = currentTotalSeconds;
        this.initTotalSeconds = initTotalSeconds;
    }

    public Counter(String name, long initTotalSeconds) {
        assertNotBlank(name);
        this.id = UUID.randomUUID();
        this.name = name;

        assertNotNegative(initTotalSeconds);
        this.currentTotalSeconds = initTotalSeconds;
        this.initTotalSeconds = initTotalSeconds;
    }

    public Counter(String name, int hours, int minutes, int seconds) {
        assertNotBlank(name);
        this.id = UUID.randomUUID();
        this.name = name;

        setInitTime(hours, minutes, seconds);
        setCurrentTime(hours, minutes, seconds);
    }

    public void setName(String name) {
        assertNotBlank(name);
        this.name = name;
    }

    public void setInitTime(int hours, int minutes, int seconds) {
        assertNotNegative(hours, minutes, seconds);
        initTotalSeconds = toTotalSeconds(hours, minutes, seconds);
    }

    public void setCurrentTime(int hours, int minutes, int seconds) {
        assertNotNegative(hours, minutes, seconds);
        currentTotalSeconds = toTotalSeconds(hours, minutes, seconds);
    }

    public void plusCurrentTime(int hours, int minutes, int seconds) {
        assertNotNegative(hours, minutes, seconds);
        currentTotalSeconds += toTotalSeconds(hours, minutes, seconds);
    }

    public void minusCurrentTime(int hours, int minutes, int seconds) {
        assertNotNegative(hours, minutes, seconds);
        currentTotalSeconds = Math.max(0L, currentTotalSeconds - toTotalSeconds(hours, minutes, seconds));
    }

    public void minusOneSecond() {
        if(currentTotalSeconds > 0) --currentTotalSeconds;
    }

    public void reset() {
        currentTotalSeconds = initTotalSeconds;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCurrentTotalSeconds() {
        return currentTotalSeconds;
    }

    public long getInitTotalSeconds() {
        return initTotalSeconds;
    }

    public long getCurrentSeconds() {
        return currentTotalSeconds % 60L;
    }

    public long getCurrentMinutes() {
        return currentTotalSeconds / 60L % 60L;
    }

    public long getCurrentHours() {
        return currentTotalSeconds / 3600L;
    }

    public boolean isElapsed() {
        return currentTotalSeconds == 0;
    }

    public boolean atStart() {
        return initTotalSeconds == currentTotalSeconds;
    }

    public boolean inProgress() {
        return !isElapsed() && !atStart();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Counter counter = (Counter) o;
        return Objects.equals(id, counter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Counter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", initTotalSeconds=" + initTotalSeconds +
                ", currentTotalSeconds=" + currentTotalSeconds +
                ", currentSeconds=" + getCurrentSeconds() +
                ", currentMinutes=" + getCurrentMinutes() +
                ", currentHours=" + getCurrentHours() +
                ", timeElapsed=" + isElapsed() +
                '}';
    }


    private long toTotalSeconds(int hours, int minutes, int seconds) {
        return hours * 3600L + minutes * 60L + seconds;
    }

    private void assertNotNegative(long totalSeconds) {
        if(totalSeconds < 0) {
            throw new IncorrectTimeException("totalSeconds can't be negative. totalSeconds = " + totalSeconds,
                    "Counter.totalSeconds.negative");
        }
    }

    private void assertNotNegative(int hours, int minutes, int seconds) {
        if(hours < 0) {
            throw new IncorrectTimeException("hours can't be negative. hours = " + hours,
                    "Counter.hours.negative");
        } else if(minutes < 0) {
            throw new IncorrectTimeException("minutes can't be negative. minutes = " + minutes,
                    "Counter.minutes.negative");
        } else if(seconds < 0) {
            throw new IncorrectTimeException("seconds can't be negative. seconds = " + seconds,
                    "Counter.seconds.negative");
        }
    }

    private void assertNotBlank(String name) {
        if(name == null || name.isBlank()) {
            throw new IncorrectCounterNameException(
                    "Counter name can't be null or blank. name is '" + name + '\'',
                    "Counter.name.isBlank");
        }
    }

}
