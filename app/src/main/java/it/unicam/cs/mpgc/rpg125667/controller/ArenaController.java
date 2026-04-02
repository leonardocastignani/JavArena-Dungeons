package it.unicam.cs.mpgc.rpg125667.controller;

import java.io.IOException;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.*;

public class ArenaController {

    @FXML private Label playerNameLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label monsterNameLabel;
    @FXML private Label monsterHpLabel;
    @FXML private TextArea battleLog;
    @FXML private Button attackButton;
    @FXML private Button backButton;

    private BattleEngine engine;
    private PlayerRepository repository;

    public void initData(Player player) {
        this.repository = new PlayerRepository();
        Monster randomEnemy = MonsterFactory.generateRandomMonster();
        this.engine = new BattleEngine(player, randomEnemy);
        
        this.battleLog.clear();
        this.log("Un " + randomEnemy.getName() + " ti sbarra la strada!");
        this.updateUI();
    }

    @FXML
    protected void onAttackClick() {
        int monsterStartHp = this.engine.getMonster().getCurrentHealth();
        this.engine.executePlayerAttack();
        int playerDamage = monsterStartHp - this.engine.getMonster().getCurrentHealth();
        this.log(this.engine.getPlayer().getName() + " attacca e infligge " + playerDamage + " danni.");

        if (this.engine.isBattleOver()) {
            this.endBattle();
            return;
        }

        int playerStartHp = this.engine.getPlayer().getCurrentHealth();
        this.engine.executeMonsterAttack();
        int monsterDamage = playerStartHp - this.engine.getPlayer().getCurrentHealth();
        this.log(this.engine.getMonster().getName() + " contrattacca e infligge " + monsterDamage + " danni.");

        this.updateUI();

        if (this.engine.isBattleOver()) {
            this.endBattle();
        }
    }

    private void updateUI() {
        this.playerNameLabel.setText(this.engine.getPlayer().getName());
        this.playerHpLabel.setText("HP: " + this.engine.getPlayer().getCurrentHealth());
        
        this.monsterNameLabel.setText(this.engine.getMonster().getName());
        this.monsterHpLabel.setText("HP: " + this.engine.getMonster().getCurrentHealth());
    }

    private void log(String message) {
        this.battleLog.appendText(message + "\n");
    }

    private void endBattle() {
        this.updateUI();
        this.log("\n--- FINE BATTAGLIA ---");
        this.log(this.engine.getBattleResult());
        
        this.attackButton.setDisable(true);
        this.backButton.setDisable(false);

        if (this.engine.getPlayer().isAlive()) {
            this.repository.save(this.engine.getPlayer());
            this.log("I tuoi progressi sono stati salvati. Salute rimanente: " + this.engine.getPlayer().getCurrentHealth());
        } else {
            this.log("Sei morto... I tuoi progressi non verranno salvati.");
        }
    }

    @FXML
    protected void onBackToMenuClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) this.backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}