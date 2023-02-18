module com.bakuard.pomodoro {

    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires com.google.gson;

    exports com.bakuard.pomodoro to javafx.graphics, javafx.controls, javafx.fxml;
    exports com.bakuard.pomodoro.controller to javafx.graphics, javafx.controls, javafx.fxml;
    exports com.bakuard.pomodoro.model.counter to com.google.gson, javafx.controls, javafx.fxml, javafx.graphics;

    opens com.bakuard.pomodoro to javafx.graphics, javafx.controls, javafx.fxml;
    opens com.bakuard.pomodoro.controller to javafx.graphics, javafx.controls, javafx.fxml;
    opens com.bakuard.pomodoro.model.counter to com.google.gson, javafx.controls, javafx.fxml, javafx.graphics;

}