package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.*;
import java.util.*;

public class LoadGameController {

    @FXML private ListView<Player> playerListView;
    @FXML private Label errorLabel;
    
    private PlayerRepository repository;

    @FXML
    public void initialize() {
        this.repository = new PlayerRepository();
        List<Player> players = this.repository.findAll();
        ObservableList<Player> observablePlayers = FXCollections.observableArrayList(players);
        this.playerListView.setItems(observablePlayers);
    }

    @FXML
    protected void onLoadHeroClick() {
        Player selectedPlayer = this.playerListView.getSelectionModel().getSelectedItem();

        if (selectedPlayer == null) {
            this.errorLabel.setText("Devi selezionare un eroe dalla lista!");
            return;
        }

        this.goToArena(selectedPlayer);
    }

    @FXML
    protected void onBackToMenuClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) this.playerListView.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToArena(Player player) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/rpg125667/view/arena.fxml"));
            Parent root = loader.load();
            ArenaController arenaController = loader.getController();
            arenaController.initData(player);
            Stage stage = (Stage) this.playerListView.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}