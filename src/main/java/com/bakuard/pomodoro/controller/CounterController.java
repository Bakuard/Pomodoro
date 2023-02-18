package com.bakuard.pomodoro.controller;

import com.bakuard.pomodoro.event.AddExtraTimeEvent;
import com.bakuard.pomodoro.event.DeleteCounterEvent;
import com.bakuard.pomodoro.event.EventBus;
import com.bakuard.pomodoro.event.SubtractTimeEvent;
import com.bakuard.pomodoro.model.counter.Counter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class CounterController extends HBox {

    private final Counter counter;
    private final EventBus eventBus;
    @FXML
    private Button delete;
    @FXML
    private Button addTime;
    @FXML
    private Button subtractTime;
    @FXML
    private TextField counterName;
    @FXML
    private Spinner<Integer> hoursSpinner;
    @FXML
    private Label hoursLabel;
    @FXML
    private Spinner<Integer> minutesSpinner;
    @FXML
    private Label minutesLabel;
    @FXML
    private Spinner<Integer> secondsSpinner;
    @FXML
    private Label secondsLabel;

    public CounterController(ResourcesLoader resourcesLoader,
                             Counter counter,
                             EventBus eventBus) {
        this.counter = counter;
        this.eventBus = eventBus;

        try {
            loadTemplate(resourcesLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ImageView icon = new ImageView(resourcesLoader.getImage("garbage"));
        icon.setFitHeight(30);
        icon.setFitWidth(30);
        delete.setGraphic(icon);

        secondsSpinner.setOnMouseClicked(event -> updateCounterInitTime());
        minutesSpinner.setOnMouseClicked(event -> updateCounterInitTime());
        hoursSpinner.setOnMouseClicked(event -> updateCounterInitTime());
        counterName.setOnMousePressed(event ->
            counterName.getStyleClass().replaceAll(
                    style -> style.equalsIgnoreCase("counterName") ? "counterNameEdit" : style
            )
        );
        counterName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                counterName.getStyleClass().replaceAll(
                        style -> style.equalsIgnoreCase("counterNameEdit") ? "counterName" : style
                );
                counter.setName(counterName.getText());
            }
        });
    }

    public void update() {
        counterName.setText(counter.getName());

        hoursSpinner.setVisible(counter.atStart());
        minutesSpinner.setVisible(counter.atStart());
        secondsSpinner.setVisible(counter.atStart());
        addTime.setVisible(!counter.atStart());
        subtractTime.setVisible(!counter.atStart());

        if(counter.atStart()) {
            hoursSpinner.getValueFactory().setValue((int) counter.getCurrentHours());
            minutesSpinner.getValueFactory().setValue((int) counter.getCurrentMinutes());
            secondsSpinner.getValueFactory().setValue((int) counter.getCurrentSeconds());
        } else {
            hoursLabel.setText(Long.toString(counter.getCurrentHours()));
            minutesLabel.setText(Long.toString(counter.getCurrentMinutes()));
            secondsLabel.setText(Long.toString(counter.getCurrentSeconds()));
        }
    }

    public void delete() {
        eventBus.post(new DeleteCounterEvent(counter));
    }

    public void addTime() {
        eventBus.post(new AddExtraTimeEvent(this, 0, 1, 0));
    }

    public void subtractTime() {
        eventBus.post(new SubtractTimeEvent(this, 0, 1, 0));
    }

    public Counter getCounter() {
        return counter;
    }


    private void updateCounterInitTime() {
        counter.setInitTime(
                hoursSpinner.getValue(),
                minutesSpinner.getValue(),
                secondsSpinner.getValue()
        );
        counter.reset();
    }

    private void loadTemplate(ResourcesLoader resourcesLoader) throws IOException {
        resourcesLoader.getTemplateLoader("counterView.fxml", this, this).load();

        getStylesheets().add(
                resourcesLoader.getCssPath("counter.css")
        );
    }

}
