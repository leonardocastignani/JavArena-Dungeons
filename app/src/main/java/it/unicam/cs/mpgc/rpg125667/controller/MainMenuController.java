package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * Controller per la schermata principale del gioco (Menu di avvio).
 */
public class MainMenuController implements InjectableController {

    @FXML private Label statusLabel;

    private GameService service;

    /**
     * Inizializzazione standard FXML.
     */
    @FXML
    public void initialize() {}

    /**
     * Implementazione di {@link InjectableController} per la DI.
     *
     * @param service Il servizio di gioco istanziato nell'App.
     */
    @Override
    public void setGameService(GameService service) {
        this.service = service;
    }

    /**
     * Gestisce il click sul pulsante "Nuova Partita", navigando alla creazione personaggio.
     */
    @FXML
    protected void onNewGameClick() {
        Stage stage = (Stage) this.statusLabel.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/character-creation.fxml", this.service);
    }

    /**
     * Gestisce il click sul pulsante "Carica Partita", navigando alla selezione dei salvataggi.
     */
    @FXML
    protected void onLoadGameClick() {
        Stage stage = (Stage) this.statusLabel.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/load-game.fxml", this.service);
    }
}