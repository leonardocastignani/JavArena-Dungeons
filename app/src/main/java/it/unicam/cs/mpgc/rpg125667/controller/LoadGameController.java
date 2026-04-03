package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import java.io.*;
import java.util.*;

public class LoadGameController {

    @FXML private ListView<Player> playerListView;
    @FXML private Label errorLabel;
    @FXML private VBox detailsPanel;
    @FXML private Label detailNameLabel;
    @FXML private Label detailLevelLabel;
    @FXML private Label detailHpLabel;
    @FXML private Label detailXpLabel;
    @FXML private Label detailStatsLabel;
    @FXML private Label detailPotionsLabel;
    
    private PlayerRepository repository;

    @FXML
    public void initialize() {
        this.repository = new PlayerRepository();
        List<Player> players = this.repository.findAll();
        ObservableList<Player> observablePlayers = FXCollections.observableArrayList(players);
        this.playerListView.setItems(observablePlayers);
        this.playerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.showPlayerDetails(newValue);
            }
        });
    }

    private void showPlayerDetails(Player p) {
        this.detailsPanel.setVisible(true);
        this.errorLabel.setText("");

        this.detailNameLabel.setText(p.getName());
        this.detailLevelLabel.setText("Livello: " + p.getLevel());
        this.detailHpLabel.setText("Salute: " + p.getCurrentHealth() + " / " + p.getStats().getMaxHealth());
        this.detailXpLabel.setText("XP: " + p.getXp() + " / " + (p.getLevel() * 50));
        this.detailStatsLabel.setText("Att: " + p.getStats().getBaseAttack() + "  |  Dif: " + p.getStats().getBaseDefense());
        this.detailPotionsLabel.setText("Pozioni rimanenti: " + p.getPotions());
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