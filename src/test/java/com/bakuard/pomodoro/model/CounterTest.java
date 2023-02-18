package com.bakuard.pomodoro.model;

import com.bakuard.pomodoro.model.counter.Counter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CounterTest {

    @Test
    @DisplayName("""
            getCurrentSeconds():
             total seconds = 0
             => return 0
            """)
    public void getCurrentSeconds1() {
        Counter counter = new Counter("counter", 0);

        long actual = counter.getCurrentSeconds();

        Assertions.assertThat(actual).isZero();
    }

    @Test
    @DisplayName("""
            getCurrentSeconds():
             total seconds less than 60
             => return value equal to total seconds
            """)
    public void getCurrentSeconds2() {
        Counter counter = new Counter("counter", 59);

        long actual = counter.getCurrentSeconds();

        Assertions.assertThat(actual).isEqualTo(59);
    }

    @Test
    @DisplayName("""
            getCurrentSeconds():
             total seconds is not a multiple of 60
             => return correct result
            """)
    public void getCurrentSeconds3() {
        Counter counter = new Counter("counter", 137);

        long actual = counter.getCurrentSeconds();

        Assertions.assertThat(actual).isEqualTo(17);
    }

    @Test
    @DisplayName("""
            getCurrentSeconds():
             total seconds is a multiple of 60
             => return 0
            """)
    public void getCurrentSeconds4() {
        Counter counter = new Counter("counter", 360);

        long actual = counter.getCurrentSeconds();

        Assertions.assertThat(actual).isZero();
    }

    @Test
    @DisplayName("""
            getCurrentMinutes():
             total seconds is 0
             => return 0
            """)
    public void getCurrentMinutes1() {
        Counter counter = new Counter("counter", 0);

        long actual = counter.getCurrentMinutes();

        Assertions.assertThat(actual).isZero();
    }

    @Test
    @DisplayName("""
            getCurrentMinutes():
             total seconds less than 60
             => return 0
            """)
    public void getCurrentMinutes2() {
        Counter counter = new Counter("counter", 59);

        long actual = counter.getCurrentMinutes();

        Assertions.assertThat(actual).isZero();
    }

    @Test
    @DisplayName("""
            getCurrentMinutes():
             total seconds is not a multiple of 3600
             => return correct result
            """)
    public void getCurrentMinutes3() {
        Counter counter = new Counter("counter", 4017);

        long actual = counter.getCurrentMinutes();

        Assertions.assertThat(actual).isEqualTo(6);
    }

    @Test
    @DisplayName("""
            getCurrentMinutes():
             total seconds is a multiple of 3600
             => return 0
            """)
    public void getCurrentMinutes4() {
        Counter counter = new Counter("counter", 7200);

        long actual = counter.getCurrentMinutes();

        Assertions.assertThat(actual).isZero();
    }

    @Test
    @DisplayName("""
            getCurrentHours():
             total seconds is 0
             => return 0
            """)
    public void getCurrentHours1() {
        Counter counter = new Counter("counter", 0);

        long actual = counter.getCurrentHours();

        Assertions.assertThat(actual).isZero();
    }

    @Test
    @DisplayName("""
            getCurrentHours():
             total seconds less than 3600
             => return 0
            """)
    public void getCurrentHours2() {
        Counter counter = new Counter("counter", 3599);

        long actual = counter.getCurrentHours();

        Assertions.assertThat(actual).isZero();
    }

    @Test
    @DisplayName("""
            getCurrentHours():
             total seconds is not a multiple of 3600
             => return correct result
            """)
    public void getCurrentHours3() {
        Counter counter = new Counter("counter", 14017);

        long actual = counter.getCurrentHours();

        Assertions.assertThat(actual).isEqualTo(3);
    }

    @Test
    @DisplayName("""
            getCurrentHours():
             total seconds is a multiple of 3600
             => return correct result
            """)
    public void getCurrentHours4() {
        Counter counter = new Counter("counter", 10800);

        long actual = counter.getCurrentHours();

        Assertions.assertThat(actual).isEqualTo(3);
    }

}