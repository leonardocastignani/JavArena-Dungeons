package it.unicam.cs.mpgc.rpg125667;

import it.unicam.cs.mpgc.rpg125667.controller.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.application.*;
import javafx.stage.*;

import java.io.*;

public class App extends Application {

    private MainMenuController mainController;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JavArena Dungeons");
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml");
    }

    @Override
    public void stop() {
        if (mainController != null) mainController.closeDatabase();
    }

    public static void main(String[] args) {
        launch();
    }
}