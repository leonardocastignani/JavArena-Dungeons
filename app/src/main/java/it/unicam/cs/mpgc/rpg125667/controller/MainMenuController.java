package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.repository.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.*;

public class MainMenuController {

    @FXML private Label statusLabel;

    private PlayerRepository repository;

    @FXML
    public void initialize() {
        this.repository = new PlayerRepository();
    }

    @FXML
    protected void onNewGameClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/rpg125667/view/character-creation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) this.statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
            this.statusLabel.setText("Errore di caricamento schermata!");
        }
    }

    @FXML
    protected void onLoadGameClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/rpg125667/view/load-game.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) this.statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
            this.statusLabel.setText("Errore di caricamento schermata!");
        }
    }

    public void closeDatabase() {
        if (this.repository != null) {
            this.repository.close();
        }
    }
}