package it.unicam.cs.mpgc.rpg125667.engine;

import it.unicam.cs.mpgc.rpg125667.model.*;
import lombok.*;

@Getter
public class BattleEngine {
    
    private final Player player;
    private final Monster monster;
    private boolean isPlayerTurn;

    public BattleEngine(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        this.isPlayerTurn = true;
    }

    public void executePlayerAttack() {
        if (!this.isBattleOver() && this.isPlayerTurn) {
            this.player.attack(this.monster);
            this.isPlayerTurn = false;
        }
    }

    public void executeMonsterAttack() {
        if (!this.isBattleOver() && !this.isPlayerTurn) {
            this.monster.attack(this.player);
            this.isPlayerTurn = true;
        }
    }

    public boolean isBattleOver() {
        return !this.player.isAlive() || !this.monster.isAlive();
    }

    public String getBattleResult() {
        if (this.player.isAlive() && !this.monster.isAlive()) {
            return "Vittoria! Hai sconfitto " + this.monster.getName() + "!";
        } else if (!this.player.isAlive() && this.monster.isAlive()) {
            return "Sconfitta... Sei stato ucciso da " + this.monster.getName() + "!";
        }
        return "La battaglia è ancora in corso...";
    }
}