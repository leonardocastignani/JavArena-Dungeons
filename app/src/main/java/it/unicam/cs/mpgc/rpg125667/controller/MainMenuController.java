package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.*;
import java.util.*;

public class MainMenuController {

    @FXML private Label statusLabel;

    private PlayerRepository repository;

    @FXML
    public void initialize() {
        this.repository = new PlayerRepository();
    }

    @FXML
    protected void onNewGameClick() {
        CharacterStats stats = new CharacterStats(100, 100, 15, 5);
        Player hero = new Player("Artù", stats);
        this.repository.save(hero);
        this.goToArena(hero);
    }

    @FXML
    protected void onLoadGameClick() {
        Optional<Player> loadedOpt = this.repository.findById(1L);
        if (loadedOpt.isPresent()) {
            Player hero = loadedOpt.get();
            this.goToArena(hero);
        } else {
            this.statusLabel.setText("Nessun salvataggio trovato!");
        }
    }

    private void goToArena(Player player) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/rpg125667/view/arena.fxml"));
            Parent root = loader.load();

            ArenaController arenaController = loader.getController();
            arenaController.initData(player);

            Stage stage = (Stage) statusLabel.getScene().getWindow();

            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Errore di caricamento dell'Arena!");
        }
    }

    public void closeDatabase() {
        if (this.repository != null) {
            this.repository.close();
        }
    }
}