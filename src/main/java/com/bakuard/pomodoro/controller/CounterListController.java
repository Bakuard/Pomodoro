package com.bakuard.pomodoro.controller;

import com.bakuard.pomodoro.event.*;
import com.bakuard.pomodoro.model.clock.Clock;
import com.bakuard.pomodoro.model.clock.ClockState;
import com.bakuard.pomodoro.model.counter.Counter;
import com.bakuard.pomodoro.model.counter.CounterList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class CounterListController {

    @FXML
    private GridPane container;
    @FXML
    private VBox countersListContainer;
    @FXML
    private Button buttonForControl;
    private ResourcesLoader resourcesLoader;
    private CounterList counterList;
    private Clock clock;
    private EventBus eventBus;

    public CounterListController(ResourcesLoader resourcesLoader,
                                 CounterList counterList,
                                 Clock clock,
                                 EventBus eventBus) {
        this.resourcesLoader = resourcesLoader;
        this.counterList = counterList;
        this.clock = clock;
        this.eventBus = eventBus;

        try {
            loadTemplate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        counterList.stream().forEach(counter -> {
            CounterController counterController = createCounterView(counter);
            countersListContainer.getChildren().add(counterController);
        });
    }

    public void addNewCounter() {
        Counter counter = counterList.add(
                resourcesLoader.getString("counter.defaultName")
        );
        CounterController counterController = createCounterView(counter);
        countersListContainer.getChildren().add(counterController);
    }

    public void deleteCounter(Counter counter) {
        counterList.asListItem(counter).
                ifPresent(counterAsListItem ->
                        countersListContainer.getChildren().remove(counterAsListItem.index())
                );
    }

    public void changeControlButtonLabel() {
        Platform.runLater(() -> {
            if(counterList.isActiveCounterElapsed()) {
                buttonForControl.setText(resourcesLoader.getString("leftPanel.button.nextTimer"));
            } else if(clock.getState() == ClockState.PAUSE) {
                buttonForControl.setText(resourcesLoader.getString("leftPanel.button.startTimer"));
            } else if(clock.getState() == ClockState.RUNNABLE) {
                buttonForControl.setText(resourcesLoader.getString("leftPanel.button.pauseTimer"));
            }
        });
    }

    public void startPauseOrResumeTimer() {
        if(counterList.isActiveCounterElapsed()) {
            Counter counter = counterList.getActiveCounter().orElseThrow();
            eventBus.post(new NextCounterEvent(counter));
        } else if(clock.getState() == ClockState.PAUSE) {
            eventBus.post(new StartTimerEvent());
        } else if(clock.getState() == ClockState.RUNNABLE) {
            eventBus.post(new PauseTimerEvent());
        }
    }

    public void resetAllCountersView() {
        eventBus.post(new ResetAllCountersEvent());
        counterList.streamWithIndexes().
                forEach(counterListItem -> {
                    CounterController counterController = (CounterController) countersListContainer.getChildren().
                            get(counterListItem.index());
                    counterController.update();
                });
    }

    public void resetCounterView(Counter counter) {
        counterList.asListItem(counter).
                ifPresent(counterAsListItem -> {
                    CounterController counterController = (CounterController) countersListContainer.getChildren().
                            get(counterAsListItem.index());
                    counterController.update();
                });
    }

    public void updateActiveCounterView() {
        counterList.getActiveCounter().
                flatMap(counterList::asListItem).
                ifPresent(counterAsListItem ->
                        Platform.runLater(() -> {
                            CounterController counterController = (CounterController) countersListContainer.getChildren().
                                    get(counterAsListItem.index());
                            counterController.update();
                        })
                );
    }

    public Parent getContainer() {
        return container;
    }


    private void loadTemplate() throws IOException {
        resourcesLoader.getTemplateLoader("timer.fxml", this).load();

        container.getStylesheets().addAll(
                resourcesLoader.getCssPath("timer.css")
        );
    }

    private CounterController createCounterView(Counter counter) {
        CounterController counterController = new CounterController(resourcesLoader, counter, eventBus);
        counterController.update();
        return counterController;
    }

}
