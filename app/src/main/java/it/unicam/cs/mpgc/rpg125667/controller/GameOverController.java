package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.repository.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class GameOverController implements InjectableController {

    @FXML private Button menuButton;

    private IPlayerRepository repository;

    @Override
    public void setRepository(IPlayerRepository repository) {
        this.repository = repository;
    }

    @FXML
    protected void onBackToMenuClick() {
        Stage stage = (Stage) menuButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.repository);
    }
}