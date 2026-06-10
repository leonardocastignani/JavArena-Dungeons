package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class GameOverController {

    @FXML private Button menuButton;

    @FXML
    protected void onBackToMenuClick() {
        Stage stage = (Stage) menuButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml");
    }
}