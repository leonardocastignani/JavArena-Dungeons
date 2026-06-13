package it.unicam.cs.mpgc.rpg125667;

import it.unicam.cs.mpgc.rpg125667.repository.*;
import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;
import it.unicam.cs.mpgc.rpg125667.engine.*;

import javafx.application.*;
import javafx.stage.*;

import lombok.extern.slf4j.*;

import java.io.*;
import java.util.concurrent.*;

@Slf4j
public class App extends Application {

    private GameService gameService;

    @Override
    public void init() {
        log.info("Inizializzazione motore di gioco...");
        CompletableFuture.runAsync(() -> {
            MonsterFactory.loadMonsters();
        });
        this.gameService = new GameService(new JsonPlayerRepository());
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JavArena Dungeons");
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.gameService);
    }

    @Override
    public void stop() {
        if (this.gameService != null) this.gameService.shutdown();
    }

    public static void main(String[] args) {
        launch();
    }
}