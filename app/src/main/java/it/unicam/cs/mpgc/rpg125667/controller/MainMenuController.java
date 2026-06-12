package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.repository.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class MainMenuController {

    @FXML private Label statusLabel;

    private IPlayerRepository repository;

    @FXML
    public void initialize() {
        this.repository = ServiceLocator.getPlayerRepository();
    }

    @FXML
    protected void onNewGameClick() {
        Stage stage = (Stage) this.statusLabel.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/character-creation.fxml");
    }

    @FXML
    protected void onLoadGameClick() {
        Stage stage = (Stage) this.statusLabel.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/load-game.fxml");
    }

    public void closeDatabase() {
        if (this.repository != null) {
            this.repository.close();
        }
    }
}