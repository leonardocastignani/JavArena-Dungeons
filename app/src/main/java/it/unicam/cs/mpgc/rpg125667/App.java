package it.unicam.cs.mpgc.rpg125667;

import it.unicam.cs.mpgc.rpg125667.repository.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.application.*;
import javafx.stage.*;

import java.io.*;

public class App extends Application {

    private IPlayerRepository repository;

    @Override
    public void init() {
        this.repository = new JsonPlayerRepository();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JavArena Dungeons");
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.repository);
    }

    @Override
    public void stop() {
        if (this.repository != null) this.repository.close();
    }

    public static void main(String[] args) {
        launch();
    }
}