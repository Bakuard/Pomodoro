package com.bakuard.pomodoro;

import com.bakuard.pomodoro.dal.CounterListRepositoryFile;
import com.bakuard.pomodoro.event.*;
import com.bakuard.pomodoro.model.counter.Counter;
import com.bakuard.pomodoro.model.counter.CounterList;
import com.bakuard.pomodoro.model.repository.CounterListRepository;
import com.bakuard.pomodoro.model.clock.Clock;
import com.bakuard.pomodoro.controller.CounterListController;
import com.bakuard.pomodoro.controller.ResourcesLoader;
import com.bakuard.pomodoro.controller.TimerSignalPlayer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("Start application");

        Application.launch(args);
    }


    private ResourcesLoader resourcesLoader;
    private TimerSignalPlayer timerSignalPlayer;
    private CounterListController counterListController;
    private CounterListRepository counterListRepository;
    private CounterList counterList;
    private Clock clock;
    private EventBus eventBus;

    public Main() {

    }

    @Override
    public void init() throws Exception {
        logger.info("Start model and view initialization");

        eventBus = new EventBus();
        resourcesLoader = new ResourcesLoader();
        timerSignalPlayer = new TimerSignalPlayer(resourcesLoader);
        counterListRepository = new CounterListRepositoryFile();
        counterList = counterListRepository.findAll().findFirst().orElse(new CounterList());
        clock = new Clock(() -> {
            eventBus.post(new TimeUpdateEvent());
            if(counterList.isActiveCounterElapsed()) {
                eventBus.post(new TimeElapsedEvent());
            }
        });
        counterListController = new CounterListController(resourcesLoader, counterList, clock, eventBus);

        eventBus.register(TimeUpdateEvent.class, event -> {
            counterList.getActiveCounter().ifPresent(Counter::minusOneSecond);
            counterListController.updateActiveCounterView();
        });
        eventBus.register(TimeElapsedEvent.class, event -> {
            logger.info("Time elapsed");

            clock.pause();
            timerSignalPlayer.start();
            counterListController.changeControlButtonLabel();
        });
        eventBus.register(AddExtraTimeEvent.class, event -> {
            AddExtraTimeEvent e = (AddExtraTimeEvent) event;
            logger.info("Add extra time: counter={}, hours={}, minutes={}, seconds={}",
                    e.counterController().getCounter(), e.hours(), e.minutes(), e.seconds());

            Counter counter = e.counterController().getCounter();
            counter.plusCurrentTime(e.hours(), e.minutes(), e.seconds());
            e.counterController().update();

            timerSignalPlayer.stop();
            clock.start();
            counterListController.changeControlButtonLabel();
        });
        eventBus.register(SubtractTimeEvent.class, event -> {
            SubtractTimeEvent e = (SubtractTimeEvent) event;
            logger.info("Subtract time: counter={}, hours={}, minutes={}, seconds={}",
                    e.counterController().getCounter(), e.hours(), e.minutes(), e.seconds());

            Counter counter = e.counterController().getCounter();
            counter.minusCurrentTime(e.hours(), e.minutes(), e.seconds());
            e.counterController().update();
        });
        eventBus.register(DeleteCounterEvent.class, event -> {
            DeleteCounterEvent e = (DeleteCounterEvent) event;
            logger.info("Delete counter={}", e.counter());

            if(counterList.isActive(e.counter())) {
                clock.pause();
                timerSignalPlayer.stop();
            }
            counterListController.deleteCounter(e.counter());
            counterList.remove(e.counter());
            counterListController.changeControlButtonLabel();
        });
        eventBus.register(NextCounterEvent.class, event -> {
            NextCounterEvent e = (NextCounterEvent) event;
            logger.info("Next counter. Recent active counter={}", e.recentActiveCounter());

            timerSignalPlayer.stop();
            counterList.nextActiveCounter();
            counterListController.resetCounterView(e.recentActiveCounter());
            counterListController.changeControlButtonLabel();
            clock.start();
        });
        eventBus.register(StartTimerEvent.class, event -> {
            logger.info("Start timer. Counter list is empty: {}", counterList.isEmpty());

            if(!counterList.isEmpty()) {
                counterListController.changeControlButtonLabel();
                clock.start();
            }
        });
        eventBus.register(PauseTimerEvent.class, event -> {
            logger.info("Pause timer");

            counterListController.changeControlButtonLabel();
            clock.pause();
        });
        eventBus.register(ResetAllCountersEvent.class, event ->  {
            logger.info("Reset all counters");

            clock.pause();
            counterList.resetAll();
            timerSignalPlayer.stop();
            counterListController.changeControlButtonLabel();
        });
    }

    @Override
    public void start(Stage stage) throws Exception {
        Thread.currentThread().setUncaughtExceptionHandler(
                (Thread thread, Throwable exception) -> logger.error("Unexpected exception.", exception)
        );

        logger.info("Start UI initialization");

        stage.setTitle(resourcesLoader.getString("title"));
        stage.setScene(new Scene(counterListController.getContainer()));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        logger.info("Save counter list and stop application");

        clock.pause();
        if(counterList.isActiveCounterElapsed()) counterList.nextActiveCounter();
        counterListRepository.save(counterList);
    }

}