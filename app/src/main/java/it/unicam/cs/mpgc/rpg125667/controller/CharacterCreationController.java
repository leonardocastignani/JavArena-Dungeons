package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.io.*;
import java.util.Random;

public class CharacterCreationController {

    @FXML private TextField nameField;
    @FXML private Label errorLabel;
    @FXML private Label attackLabel;
    @FXML private Label defenseLabel;

    private PlayerRepository repository;
    private Random random;

    private int currentAttack;
    private int currentDefense;

    @FXML
    public void initialize() {
        this.repository = new PlayerRepository();
        this.random = new Random();
        this.rollStats();
    }

    @FXML
    protected void onRollStatsClick() {
        this.rollStats();
    }

    private void rollStats() {
        this.currentAttack = this.random.nextInt(11) + 10; 
        this.currentDefense = this.random.nextInt(7) + 2;

        this.attackLabel.setText(String.valueOf(this.currentAttack));
        this.defenseLabel.setText(String.valueOf(this.currentDefense));
    }

    @FXML
    protected void onCreateHeroClick() {
        String heroName = this.nameField.getText().trim();

        if (heroName.isEmpty() || heroName == null) {
            this.errorLabel.setText("Devi inserire un nome per iniziare!");
            return;
        }

        CharacterStats stats = new CharacterStats(100, 100, this.currentAttack, this.currentDefense);
        Player newHero = new Player(heroName, stats);

        this.repository.save(newHero);
        this.repository.close();

        this.goToArena(newHero);
    }

    @FXML
    protected void onBackToMenuClick() {
        this.repository.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) this.nameField.getScene().getWindow();
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

            Stage stage = (Stage) this.nameField.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}