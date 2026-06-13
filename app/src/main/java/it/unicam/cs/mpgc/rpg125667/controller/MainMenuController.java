package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class MainMenuController implements InjectableController {

    @FXML private Label statusLabel;

    private GameService service;

    @FXML
    public void initialize() {}

    @Override
    public void setGameService(GameService service) {
        this.service = service;
    }

    @FXML
    protected void onNewGameClick() {
        Stage stage = (Stage) this.statusLabel.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/character-creation.fxml", this.service);
    }

    @FXML
    protected void onLoadGameClick() {
        Stage stage = (Stage) this.statusLabel.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/load-game.fxml", this.service);
    }
}