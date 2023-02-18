package com.bakuard.pomodoro.model.repository;

import com.bakuard.pomodoro.model.counter.CounterList;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public interface CounterListRepository {

    public void save(CounterList counterList);

    public void deleteById(UUID counterListId);

    public Optional<CounterList> findById(UUID counterListId);

    public Stream<CounterList> findAll();

    public long count();

}
