package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;
import javafx.fxml.*;
import javafx.scene.control.*;

public class ArenaController {

    @FXML private Label playerNameLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label monsterNameLabel;
    @FXML private Label monsterHpLabel;
    @FXML private TextArea battleLog;
    @FXML private Button attackButton;

    private BattleEngine engine;

    public void initData(Player player) {
        CharacterStats goblinStats = new CharacterStats(50, 50, 10, 2);
        Monster goblin = new Monster("Goblin Infuriato", goblinStats);
        this.engine = new BattleEngine(player, goblin);
        this.battleLog.clear();
        this.log("Un " + goblin.getName() + " ti sbarra la strada!");
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
    }
}