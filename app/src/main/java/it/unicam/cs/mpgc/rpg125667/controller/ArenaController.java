package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

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
    private PlayerRepository repository;

    public void initData(Player player) {
        this.repository = new PlayerRepository();
        Monster randomEnemy = MonsterFactory.generateRandomMonster(player.getLevel());
        this.engine = new BattleEngine(player, randomEnemy);
        
        this.battleLog.clear();
        System.out.println("\n========================================");
        this.log("Un " + randomEnemy.getName() + " ti sbarra la strada!");
        this.log("--- INIZIO BATTAGLIA ---");
        this.updateUI();
    }

    @FXML
    protected void onAttackClick() {
        String playerLog = this.engine.executePlayerAttack();
        this.log(playerLog);

        if (this.engine.isBattleOver()) {
            this.endBattle();
            return;
        }

        String monsterLog = this.engine.executeMonsterAttack();
        this.log(monsterLog);

        this.updateUI();

        if (this.engine.isBattleOver()) this.endBattle();
    }

    @FXML
    protected void onHealClick() {
        if (this.engine.getPlayer().usePotion()) {
            this.log(this.engine.getPlayer().getName() + " beve una pozione e recupera 30 HP!");

            String monsterLog = this.engine.executeMonsterAttack();
            this.log(monsterLog);

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
        this.healButton.setDisable(this.engine.getPlayer().getPotions() <= 0);
    }

    private void log(String message) {
        this.battleLog.appendText(message + "\n");
        System.out.println(message);
    }

    private void endBattle() {
        this.updateUI();
        this.log("\n--- FINE BATTAGLIA ---");
        this.log(this.engine.getBattleResult());
        
        this.attackButton.setDisable(true);
        this.healButton.setDisable(true);

        if (this.engine.getPlayer().isAlive()) {
            this.backButton.setDisable(false);

            String rewardLog = this.engine.grantRewards();
            this.log(rewardLog);

            this.updateUI();

            this.repository.save(this.engine.getPlayer());
            this.log("I tuoi progressi sono stati salvati. Salute rimanente: " + this.engine.getPlayer().getCurrentHealth() + " HP.");
        } else {
            this.log("Sei morto... I tuoi progressi non verranno salvati.");
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