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
 * <p>
 * Questa classe gestisce il ciclo di vita (lifecycle) dell'applicazione,
 * orchestrando l'inizializzazione dei servizi, il caricamento asincrono delle risorse
 * e la corretta chiusura delle operazioni di I/O.
 * </p>
 */
@Slf4j
public class App extends Application {

    private GameService gameService;

    /**
     * Metodo di bootstrap del ciclo di vita JavaFX.
     * <p>
     * Esegue il caricamento delle risorse pesanti (es. mostri tramite {@link MonsterLoader})
     * in modo asincrono tramite {@link CompletableFuture} per evitare il congelamento
     * dell'interfaccia utente all'avvio. Inizializza inoltre il {@link GameService}
     * con l'implementazione del repository su file ({@link JsonPlayerRepository}).
     * </p>
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
     * Avvia lo stage principale dell'applicazione.
     * <p>
     * Configura il titolo della finestra e delega al {@link SceneManager} 
     * il caricamento della scena iniziale (menu principale).
     * </p>
     *
     * @param stage La finestra principale (Stage) fornita dal runtime di JavaFX.
     * @throws IOException se si verifica un errore durante il caricamento della risorsa FXML del menu principale.
     */
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("JavArena Dungeons");
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.gameService);
    }

    /**
     * Metodo invocato alla chiusura dell'applicazione.
     * <p>
     * Garantisce un "graceful shutdown" invocando il metodo {@code shutdown()}
     * del {@link GameService}. Questo è critico per assicurare che le operazioni di
     * salvataggio pendenti vengano completate correttamente prima della chiusura del processo.
     * </p>
     */
    @Override
    public void stop() {
        if (this.gameService != null) this.gameService.shutdown();
    }

    /**
     * Metodo standard di lancio Java, punto di ingresso della JVM.
     * <p>
     * Inoltra gli argomenti da riga di comando al runtime JavaFX tramite
     * {@link Application#launch(String...)}, rendendoli disponibili tramite
     * {@link Application#getParameters()} nei metodi del ciclo di vita.
     * </p>
     *
     * @param args Argomenti passati da riga di comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}