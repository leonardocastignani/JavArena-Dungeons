package it.unicam.cs.mpgc.rpg125667.engine;

import it.unicam.cs.mpgc.rpg125667.model.*;
import lombok.*;
import java.util.*;

@Getter
public class BattleEngine {
    
    private final Player player;
    private final Monster monster;
    private boolean isPlayerTurn;
    private final Random random;

    public BattleEngine(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        this.isPlayerTurn = true;
        this.random = new Random();
    }

    public String executePlayerAttack() {
        if (!this.isBattleOver() && this.isPlayerTurn) {
            this.isPlayerTurn = false;
            return this.resolveStrike(this.player, this.monster);
        }
        return null;
    }

    public String executeMonsterAttack() {
        if (!this.isBattleOver() && !this.isPlayerTurn) {
            this.isPlayerTurn = true;
            return this.resolveStrike(this.monster, this.player);
        }
        return null;
    }

    private String resolveStrike(Combatant attacker, Combatant defender) {
        int roll = this.random.nextInt(100) + 1;

        if (roll <= 10) return "SCHIVATA! " + defender.getName() + " evita agilmente l'attacco di " + attacker.getName() + "!";

        int baseDmg = attacker.getStats().getBaseAttack();
        boolean isCrit = false;

        if (roll > 90) {
            baseDmg *= 2;
            isCrit = true;
        }

        int startHp = defender.getCurrentHealth();
        defender.takeDamage(baseDmg);
        int actualDamage = startHp - defender.getCurrentHealth();

        if (isCrit) {
            return "CRITICO! " + attacker.getName() + " sferra un colpo micidiale e infligge " + actualDamage + " danni!";
        } else {
            return attacker.getName() + " attacca e infligge " + actualDamage + " danni.";
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

    public String grantRewards() {
        if (!this.player.isAlive() || this.monster.isAlive()) return "";

        int xpReward = 20 + (this.monster.getStats().getMaxHealth() / 2);
        boolean leveledUp = this.player.gainXp(xpReward);

        StringBuilder sb = new StringBuilder();
        sb.append("Hai ottenuto ").append(xpReward).append(" punti esperienza!");
        
        if (leveledUp) {
            sb.append("\nSALI DI LIVELLO! Sei ora al Livello ").append(this.player.getLevel()).append("!");
            sb.append("\nSalute e pozioni ripristinate. Statistiche aumentate!");
        }

        return sb.toString();
    }
}