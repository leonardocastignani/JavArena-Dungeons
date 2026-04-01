package it.unicam.cs.mpgc.rpg125667.model;

public class CharacterStats {
    private final int maxHealth;
    private int currentHealth;
    private int baseAttack;
    private int baseDefense;

    public CharacterStats(int maxHealth, int baseAttack, int baseDefense) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getCurrentHealth() {
        return this.currentHealth;
    }

    public int getBaseAttack() {
        return this.baseAttack;
    }

    public int getBaseDefense() {
        return this.baseDefense;
    }

    public void reduceHealth(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.max(0, this.currentHealth - amount);
        }
    }
}