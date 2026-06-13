package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.repository.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class MainMenuController implements InjectableController {

    @FXML private Label statusLabel;

    private IPlayerRepository repository;

    @FXML
    public void initialize() {}

    @Override
    public void setRepository(IPlayerRepository repository) {
        this.repository = repository;
    }

    @FXML
    protected void onNewGameClick() {
        Stage stage = (Stage) this.statusLabel.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/character-creation.fxml", this.repository);
    }

    @FXML
    protected void onLoadGameClick() {
        Stage stage = (Stage) this.statusLabel.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/load-game.fxml", this.repository);
    }
}