package com.bakuard.pomodoro.model.counter;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CounterList {

    private final UUID id;
    private final List<Counter> counters;
    private Counter activeCounter;

    public CounterList() {
        this.id = UUID.randomUUID();
        this.counters = new ArrayList<>();
    }

    public CounterList(UUID id, List<Counter> counters, UUID activeCounterId) {
        this.id = id;
        this.counters = counters;
        setActiveCounter(activeCounterId);
    }

    public UUID getId() {
        return id;
    }

    public Counter add(String defaultCounterName) {
        Counter counter = new Counter(defaultCounterName, 0, 10, 0);
        if(counters.isEmpty()) activeCounter = counter;
        counters.add(counter);
        return counter;
    }

    public void remove(Counter counter) {
        if(counters.size() > 1 && activeCounter.equals(counter)) nextActiveCounter();
        else if(counters.size() <= 1) activeCounter = null;

        counters.removeIf(c -> c.equals(counter));
    }

    public void resetAll() {
        counters.forEach(Counter::reset);
        if(!counters.isEmpty()) activeCounter = counters.get(0);
        else activeCounter = null;
    }

    public Stream<Counter> stream() {
        return counters.stream();
    }

    public Stream<CounterAsListItem> streamWithIndexes() {
        return IntStream.range(0, counters.size()).
                mapToObj(index -> {
                    Counter counter = counters.get(index);
                    return new CounterAsListItem(index, counter, activeCounter.equals(counter));
                });
    }

    public Optional<CounterAsListItem> asListItem(Counter counter) {
        return IntStream.range(0, counters.size()).
                filter(i -> counters.get(i).equals(counter)).
                mapToObj(i -> new CounterAsListItem(i, counter, activeCounter.equals(counter))).
                findFirst();
    }

    public boolean isEmpty() {
        return counters.isEmpty();
    }


    public void nextActiveCounter() {
        if(counters.isEmpty()) activeCounter = null;
        else {
            activeCounter.reset();
            int index = counters.indexOf(activeCounter);
            activeCounter = counters.get(++index % counters.size());
        }
    }

    public void setActiveCounter(UUID counterId) {
        activeCounter = counters.stream().
                filter(counter -> counter.getId().equals(counterId)).
                findFirst().
                orElse(null);
    }

    public Optional<Counter> getActiveCounter() {
        return Optional.ofNullable(activeCounter);
    }

    public boolean isActiveCounterElapsed() {
        return getActiveCounter().map(Counter::isElapsed).orElse(false);
    }

    public boolean isActive(Counter counter) {
        return getActiveCounter().
                map(activeCounter -> activeCounter.equals(counter)).
                orElse(false);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CounterList that = (CounterList) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CounterList{" +
                "id=" + id +
                ", counters=" + counters +
                ", activeCounter=" + activeCounter +
                '}';
    }

}
