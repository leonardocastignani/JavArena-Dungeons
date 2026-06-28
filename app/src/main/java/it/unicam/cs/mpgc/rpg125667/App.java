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

/**
 * Entry point principale dell'applicazione JavaFX.
 * Inizializza i servizi asincroni e coordina il ciclo di vita (start, stop).
 */
@Slf4j
public class App extends Application {

    private GameService gameService;

    /**
     * Metodo di bootstrap. Carica le risorse (i mostri) asincronamente
     * e inizializza la connessione al database.
     */
    @Override
    public void init() {
        log.info("Inizializzazione motore di gioco...");
        CompletableFuture.runAsync(() -> {
            MonsterLoader.loadMonsters();
        });
        this.gameService = new GameService(new JsonPlayerRepository());
    }

    /**
     * Avvia lo stage principale e carica il menu.
     *
     * @param stage La finestra principale fornita dal runtime di JavaFX.
     */
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JavArena Dungeons");
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.gameService);
    }

    /**
     * Chiamato quando l'utente chiude l'applicazione. Invoca il graceful shutdown.
     */
    @Override
    public void stop() {
        if (this.gameService != null) this.gameService.shutdown();
    }

    /**
     * Metodo standard di lancio Java.
     *
     * @param args Argomenti passati da riga di comando.
     */
    public static void main(String[] args) {
        launch();
    }
}