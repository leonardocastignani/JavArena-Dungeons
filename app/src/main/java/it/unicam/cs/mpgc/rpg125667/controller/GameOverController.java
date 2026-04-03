package it.unicam.cs.mpgc.rpg125667.controller;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.*;

public class GameOverController {

    @FXML private Button menuButton;

    @FXML
    protected void onBackToMenuClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) this.menuButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}