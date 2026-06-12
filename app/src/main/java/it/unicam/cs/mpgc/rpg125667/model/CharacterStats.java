package it.unicam.cs.mpgc.rpg125667.model;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterStats {
    private int maxHealth;
    private int currentHealth;
    private int baseAttack;
    private int baseDefense;

    public void reduceHealth(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.max(0, this.currentHealth - amount);
        }
    }

    public void heal(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.min(this.maxHealth, this.currentHealth + amount);
        }
    }

    public void fullHeal() {
        this.currentHealth = this.maxHealth;
    }

    public void upgradeStats(int healthBonus, int attackBonus, int defenseBonus) {
        this.maxHealth += healthBonus;
        this.baseAttack += attackBonus;
        this.baseDefense += defenseBonus;
        this.fullHeal();
    }
}