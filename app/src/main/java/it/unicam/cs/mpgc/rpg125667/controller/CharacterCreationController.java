package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

import java.util.*;

public class CharacterCreationController implements InjectableController {

    @FXML private TextField nameField;
    @FXML private Label errorLabel;
    @FXML private Label attackLabel;
    @FXML private Label defenseLabel;

    private GameService service;
    private Random random;

    private int currentAttack;
    private int currentDefense;

    @FXML
    public void initialize() {
        this.random = new Random();
        this.rollStats();
    }

    @Override
    public void setGameService(GameService service) {
        this.service = service;
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

        if (heroName == null || heroName.trim().isEmpty()) {
            this.errorLabel.setText("Errore: Il nome non può essere vuoto.");
            return;
        }

        heroName = heroName.trim();

        if (heroName.length() < 3 || heroName.length() > 15) {
            this.errorLabel.setText("Errore: Il nome deve avere tra 3 e 15 caratteri.");
            return;
        }
        if (!heroName.matches("^[a-zA-Z0-9 ]+$")) {
            this.errorLabel.setText("Errore: Ammessi solo caratteri alfanumerici.");
            return;
        }

        CharacterStats stats = new CharacterStats(100, 100, this.currentAttack, this.currentDefense);
        Player newHero = new Player(heroName, stats);

        this.service.saveProgress(newHero);

        this.goToArena(newHero);
    }

    @FXML
    protected void onBackToMenuClick() {
        Stage stage = (Stage) this.nameField.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.service);
    }

    private void goToArena(Player player) {
        Stage stage = (Stage) this.nameField.getScene().getWindow();
        ArenaController arenaController = SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/arena.fxml", this.service);
        arenaController.initData(player);
    }
}