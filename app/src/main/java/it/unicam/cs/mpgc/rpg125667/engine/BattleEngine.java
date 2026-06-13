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

    public BattleEngine(Player player, Monster monster, Random random) {
        this.player = player;
        this.monster = monster;
        this.isPlayerTurn = true;
        this.random = random;
    }

    public BattleEngine(Player player, Monster monster) {
        this(player, monster, new Random());
    }

    public TurnResult executePlayerAttack() {
        int attackRoll = this.random.nextInt(100) + 1;

        if (attackRoll <= 10) {
            String log = "SCHIVATA! " + this.monster.getName() + " evita agilmente l'attacco di " + this.player.getName() + "!";
            return new TurnResult(log, 0, false, true);
        }

        int damage = Math.max(1, this.player.getStats().getBaseAttack() - this.monster.getStats().getBaseDefense());

        if (attackRoll > 90) {
            damage *= 2;
            this.monster.getStats().reduceHealth(damage);
            String log = "CRITICO! " + this.player.getName() + " sferra un colpo micidiale e infligge " + damage + " danni!";
            return new TurnResult(log, damage, true, false);
        }

        this.monster.getStats().reduceHealth(damage);
        String log = this.player.getName() + " attacca e infligge " + damage + " danni.";
        return new TurnResult(log, damage, false, false);
    }

    public TurnResult executeMonsterAttack() {
        int attackRoll = this.random.nextInt(100) + 1;

        if (attackRoll <= 10) {
            String log = "SCHIVATA! " + this.player.getName() + " evita agilmente l'attacco di " + this.monster.getName() + "!";
            return new TurnResult(log, 0, false, true);
        }

        int damage = Math.max(1, this.monster.getStats().getBaseAttack() - this.player.getStats().getBaseDefense());

        if (attackRoll > 90) {
            damage *= 2;
            this.player.getStats().reduceHealth(damage);
            String log = "CRITICO! " + this.monster.getName() + " sferra un colpo micidiale e infligge " + damage + " danni!";
            return new TurnResult(log, damage, true, false);
        }

        this.player.getStats().reduceHealth(damage);
        String log = this.monster.getName() + " attacca e infligge " + damage + " danni.";
        return new TurnResult(log, damage, false, false);
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