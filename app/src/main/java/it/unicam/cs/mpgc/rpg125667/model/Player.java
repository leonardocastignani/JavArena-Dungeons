package it.unicam.cs.mpgc.rpg125667.model;

import com.fasterxml.jackson.annotation.*;

import it.unicam.cs.mpgc.rpg125667.util.*;

import lombok.*;

import java.util.*;

@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Player implements Combatant {

    private String id;
    private String name;
    private CharacterStats stats;
    private int potions = 3;
    private int level = 1;
    private int xp = 0;

    public Player(String name, CharacterStats stats) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.stats = stats;
    }

    @Override
    @JsonIgnore
    public int getCurrentHealth() {
        return this.stats.getCurrentHealth();
    }

    @Override
    @JsonIgnore
    public boolean isAlive() {
        return this.stats.getCurrentHealth() > 0;
    }

    @Override
    public int takeDamage(int rawDamage) {
        int actualDamage = Math.max(GameConfig.MIN_DAMAGE, rawDamage - this.stats.getBaseDefense());
        this.stats.reduceHealth(actualDamage);
        return actualDamage;
    }

    public boolean usePotion() {
        if (this.potions > 0 && this.stats.getCurrentHealth() < this.stats.getMaxHealth()) {
            this.potions--;
            int healAmount = (int) (this.stats.getMaxHealth() * GameConfig.POTION_HEAL_PERCENTAGE);
            this.stats.heal(healAmount);
            return true;
        }
        return false;
    }

    public boolean gainXp(int amount) {
        this.xp += amount;
        boolean leveledUp = false;
        
        while (this.xp >= (this.level * GameConfig.LEVEL_UP_XP_MULTIPLIER)) {
            this.xp -= (this.level * GameConfig.LEVEL_UP_XP_MULTIPLIER);
            this.levelUp();
            leveledUp = true;
        }
        return leveledUp;
    }

    private void levelUp() {
        this.level++;
        this.stats.upgradeStats(GameConfig.HP_BONUS_PER_LEVEL, GameConfig.ATK_BONUS_PER_LEVEL, GameConfig.DEF_BONUS_PER_LEVEL);
        this.potions = GameConfig.BASE_POTIONS;
    }

    @Override
    public String toString() {
        return this.name + " (Lvl. " + this.level + ")";
    }
}