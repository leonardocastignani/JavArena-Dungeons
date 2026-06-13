package it.unicam.cs.mpgc.rpg125667;

import it.unicam.cs.mpgc.rpg125667.repository.*;
import it.unicam.cs.mpgc.rpg125667.util.*;
import it.unicam.cs.mpgc.rpg125667.engine.*;

import javafx.application.*;
import javafx.stage.*;

import lombok.extern.slf4j.*;

import java.io.*;

@Slf4j
public class App extends Application {

    private IPlayerRepository repository;

    @Override
    public void init() {
        log.info("Inizializzazione motore di gioco...");
        MonsterFactory.loadMonsters();
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