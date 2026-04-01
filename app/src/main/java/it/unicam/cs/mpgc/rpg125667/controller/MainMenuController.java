package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import java.util.*;

public class MainMenuController {

    @FXML
    private Label statusLabel;

    private PlayerRepository repository;

    @FXML
    public void initialize() {
        repository = new PlayerRepository();
    }

    @FXML
    protected void onNewGameClick() {
        CharacterStats stats = new CharacterStats(100, 100, 15, 5);
        Player hero = new Player("Artù", stats);
        repository.save(hero);
        statusLabel.setText("Nuovo eroe creato e salvato con ID: " + hero.getId());
    }

    @FXML
    protected void onLoadGameClick() {
        Optional<Player> loadedOpt = repository.findById(1L);
        if (loadedOpt.isPresent()) {
            Player p = loadedOpt.get();
            statusLabel.setText("Caricato: " + p.getName() + " (HP: " + p.getCurrentHealth() + ")");
        } else {
            statusLabel.setText("Nessun salvataggio trovato!");
        }
    }

    public void closeDatabase() {
        if (repository != null) {
            repository.close();
        }
    }
}