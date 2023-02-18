package com.bakuard.pomodoro.model.clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Clock {

    private static final Logger logger = LoggerFactory.getLogger(Clock.class.getName());


    private volatile ClockState state;
    private Thread thread;
    private final Object lock;
    private final ClockTask task;

    public Clock(ClockTask task) {
        state = ClockState.PAUSE;
        lock = new Object();
        this.task = task;
    }

    public void start() {
        state = ClockState.RUNNABLE;

        if(thread == null) {
            thread = new Thread(() -> {
                try {
                    execute();
                } catch(InterruptedException e) {
                    logger.error("Timer was interrupted. state={}", state, e);
                } catch(Exception e) {
                    logger.error("Timer failed", e);
                }
            });
            thread.setDaemon(true);
            thread.start();
        } else {
            synchronized(lock) {
                lock.notify();
            }
        }
    }

    public void pause() {
        state = ClockState.PAUSE;
    }

    public ClockState getState() {
        return state;
    }


    private void execute() throws Exception {
        final long updateInterval = 1000L;

        while(true) {
            if(state == ClockState.PAUSE) {
                synchronized(lock) {
                    while(state == ClockState.PAUSE) lock.wait(); //while(pause) - защита от спонтанного пробуждения
                }
            }

            long lastTime = System.currentTimeMillis();
            synchronized(lock) {
                task.execute();
            }
            long elapsedTime = System.currentTimeMillis() - lastTime;
            long sleepTime = updateInterval - elapsedTime;

            if(sleepTime > 0L) Thread.sleep(sleepTime);
            else logger.warn("Timer step takes too long. Elapsed time {}", elapsedTime);
        }
    }

}
