package it.unicam.cs.mpgc.rpg125667.model;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

import java.util.*;

@Getter
@Setter
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
    public void takeDamage(int damage) {
        int actualDamage = Math.max(1, damage - this.stats.getBaseDefense());
        this.stats.reduceHealth(actualDamage);
    }

    public boolean usePotion() {
        if (this.potions > 0) {
            this.potions--;
            this.stats.heal(30); 
            return true;
        }
        return false;
    }

    public boolean gainXp(int amount) {
        this.xp += amount;
        boolean leveledUp = false;
        
        while (this.xp >= (this.level * 50)) {
            this.xp -= (this.level * 50);
            this.levelUp();
            leveledUp = true;
        }
        return leveledUp;
    }

    private void levelUp() {
        this.level++;
        this.stats.setMaxHealth(this.stats.getMaxHealth() + 20);
        this.stats.setBaseAttack(this.stats.getBaseAttack() + 2);
        this.stats.setBaseDefense(this.stats.getBaseDefense() + 1);
        this.stats.setCurrentHealth(this.stats.getMaxHealth());
        this.potions = 3;
    }

    @Override
    public String toString() {
        return this.name + " (Lvl. " + this.level + ")";
    }
}