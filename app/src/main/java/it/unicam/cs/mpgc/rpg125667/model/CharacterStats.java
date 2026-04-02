package it.unicam.cs.mpgc.rpg125667.model;

import lombok.*;

@Getter
@Setter
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
}