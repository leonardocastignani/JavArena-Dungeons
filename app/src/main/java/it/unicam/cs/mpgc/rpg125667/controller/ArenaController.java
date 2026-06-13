package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import lombok.extern.slf4j.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

@Slf4j
public class ArenaController {

    @FXML private Label playerNameLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label playerStatsLabel;
    @FXML private Label monsterNameLabel;
    @FXML private Label monsterHpLabel;
    @FXML private Label monsterStatsLabel;
    @FXML private TextArea battleLog;
    @FXML private Button attackButton;
    @FXML private Button healButton;
    @FXML private Button backButton;

    private BattleEngine engine;
    private IPlayerRepository repository;

    public void initData(Player player) {
        this.repository = ServiceLocator.getPlayerRepository();
        Monster randomEnemy = MonsterFactory.generateRandomMonster(player.getLevel());
        this.engine = new BattleEngine(player, randomEnemy);
        
        this.battleLog.clear();
        log.info("Inizializzazione battaglia: {} vs {}", player.getName(), randomEnemy.getName());
        System.out.println("\n========================================");
        this.logMessage("Un " + randomEnemy.getName() + " ti sbarra la strada!");
        this.logMessage("--- INIZIO BATTAGLIA ---");
        this.updateUI();
    }

    @FXML
    protected void onAttackClick() {
        String playerLog = this.engine.executePlayerAttack();
        this.logMessage(playerLog);

        if (this.engine.isBattleOver()) {
            this.endBattle();
            return;
        }

        String monsterLog = this.engine.executeMonsterAttack();
        this.logMessage(monsterLog);

        this.updateUI();

        if (this.engine.isBattleOver()) this.endBattle();
    }

    @FXML
    protected void onHealClick() {
        if (this.engine.getPlayer().usePotion()) {
            this.logMessage(this.engine.getPlayer().getName() + " beve una pozione e recupera 30 HP!");

            if (!this.engine.isBattleOver()) {
                String monsterLog = this.engine.executeMonsterAttack();
                this.logMessage(monsterLog);
            }

            this.updateUI();

            if (this.engine.isBattleOver()) this.endBattle();
        }
    }

    private void updateUI() {
        this.playerNameLabel.setText(this.engine.getPlayer().getName()+ " (Lv. " + this.engine.getPlayer().getLevel() + ")");
        this.playerHpLabel.setText("HP: " + this.engine.getPlayer().getCurrentHealth());
        this.playerStatsLabel.setText("Att: " + this.engine.getPlayer().getStats().getBaseAttack() + 
                                 "  |  Dif: " + this.engine.getPlayer().getStats().getBaseDefense());
        
        this.monsterNameLabel.setText(this.engine.getMonster().getName());
        this.monsterHpLabel.setText("HP: " + this.engine.getMonster().getCurrentHealth());
        this.monsterStatsLabel.setText("Att: " + this.engine.getMonster().getStats().getBaseAttack() + 
                                  "  |  Dif: " + this.engine.getMonster().getStats().getBaseDefense());

        this.healButton.setText("Curati (" + this.engine.getPlayer().getPotions() + ")");
        boolean isBattleFinished = this.engine != null && this.engine.isBattleOver();
        this.healButton.setDisable(this.engine.getPlayer().getPotions() <= 0 || isBattleFinished);
    }

    private void logMessage(String message) {
        if (message == null || message.isEmpty()) return;
        this.battleLog.appendText(message + "\n");
        log.debug("Azione: {}", message);
    }

    private void endBattle() {
        this.updateUI();
        this.logMessage("\n--- FINE BATTAGLIA ---");
        this.logMessage(this.engine.getBattleResult());
        
        this.attackButton.setDisable(true);
        this.healButton.setDisable(true);

        if (this.engine.getPlayer().isAlive()) {
            this.backButton.setDisable(false);

            String rewardLog = this.engine.grantRewards();
            this.logMessage(rewardLog);

            this.updateUI();

            this.monsterNameLabel.setText("VITTORIA!");
            this.monsterNameLabel.getStyleClass().add("victory-label");
            this.monsterHpLabel.setText("Nemico annientato");
            this.monsterStatsLabel.setText("💀"); 
            
            // 2. Trasformiamo il bottone "Torna al menu" in un trofeo!
            this.backButton.setText("Torna al Menu");
            if (!this.backButton.getStyleClass().contains("victory-button")) {
                this.backButton.getStyleClass().add("victory-button");
            }

            this.repository.save(this.engine.getPlayer());
            this.logMessage("I tuoi progressi sono stati salvati. Salute rimanente: " + this.engine.getPlayer().getCurrentHealth() + " HP.");
        } else {
            this.logMessage("Sei morto... I tuoi progressi non verranno salvati.");
            this.repository.delete(this.engine.getPlayer());
            this.goToGameOver();
        }
    }

    @FXML
    protected void onBackToMenuClick() {
        Stage stage = (Stage) this.backButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml");
    }

    private void goToGameOver() {
        Stage stage = (Stage) this.attackButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/game-over.fxml");
    }
}