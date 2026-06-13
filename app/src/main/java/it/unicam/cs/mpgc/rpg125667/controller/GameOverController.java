package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class GameOverController implements InjectableController {

    @FXML private Button menuButton;

    private GameService service;

    @Override
    public void setGameService(GameService service) {
        this.service = service;
    }

    @FXML
    protected void onBackToMenuClick() {
        Stage stage = (Stage) menuButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.service);
    }
}