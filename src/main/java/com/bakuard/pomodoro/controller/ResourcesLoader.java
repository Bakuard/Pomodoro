package com.bakuard.pomodoro.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourcesLoader {

    private HashMap<String, Image> images;
    private HashMap<String, Media> sounds;
    private ResourceBundle strings;

    public ResourcesLoader() {
        images = new HashMap<>();
        sounds = new HashMap<>();
        strings = ResourceBundle.getBundle("/strings/userInterface", Locale.getDefault());
    }

    public Image getImage(String simpleName) {
        if(!images.containsKey(simpleName)) {
            Image image = new Image(
                    ResourcesLoader.class.getResourceAsStream("/images/" + simpleName + ".png")
            );
            images.put(simpleName, image);
        }
        return images.get(simpleName);
    }

    public Media getSound(String simpleFileName) {
        if(!sounds.containsKey(simpleFileName)) {
            Media media = new Media(
                    ResourcesLoader.class.getResource("/sounds/" + simpleFileName).toExternalForm()
            );
            sounds.put(simpleFileName, media);
        }
        return sounds.get(simpleFileName);
    }

    public String getString(String key) {
        return strings.getString(key);
    }

    public String getCssPath(String cssFileName) {
        return ResourcesLoader.class.getResource("/styles/" + cssFileName).toExternalForm();
    }

    public FXMLLoader getTemplateLoader(String templateName, Object controller) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ResourcesLoader.class.getResource("/templates/"+ templateName));
        loader.setController(controller);
        loader.setResources(strings);
        return loader;
    }

    public FXMLLoader getTemplateLoader(String templateName, Object controller, Object root) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ResourcesLoader.class.getResource("/templates/"+ templateName));
        loader.setController(controller);
        loader.setRoot(root);
        loader.setResources(strings);
        return loader;
    }

}
