package it.unicam.cs.mpgc.rpg125667.model;

import lombok.*;

/**
 * Incapsula tutte le statistiche numeriche vitali e di combattimento di un'entità.
 * Gestisce la logica di cura, ricezione dei danni e potenziamento dei parametri base.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterStats {
    private int maxHealth;
    private int currentHealth;
    private int baseAttack;
    private int baseDefense;

    /**
     * Riduce la salute corrente dell'entità, assicurandosi che non scenda mai sotto lo zero.
     *
     * @param amount La quantità di danni puri da sottrarre alla salute corrente.
     */
    public void reduceHealth(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.max(0, this.currentHealth - amount);
        }
    }

    /**
     * Ripristina la salute dell'entità, assicurandosi che non superi il valore massimo consentito.
     *
     * @param amount La quantità di punti vita da ripristinare.
     */
    public void heal(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.min(this.maxHealth, this.currentHealth + amount);
        }
    }

    /**
     * Ripristina completamente la salute dell'entità, portandola al massimo consentito.
     * Utilizzato principalmente dopo un combattimento o durante il riposo.
     * Garantisce che la salute corrente sia sempre coerente con il massimo definito.
     */
    public void fullHeal() {
        this.currentHealth = this.maxHealth;
    }

    /**
     * Incrementa permanentemente le statistiche base dell'entità.
     * Utilizzato primariamente durante il level-up del giocatore.
     *
     * @param hpBonus  L'incremento dei punti vita massimi (e conseguente cura completa).
     * @param atkBonus L'incremento dell'attacco base.
     * @param defBonus L'incremento della difesa base.
     */
    public void upgradeStats(int healthBonus, int attackBonus, int defenseBonus) {
        this.maxHealth += healthBonus;
        this.baseAttack += attackBonus;
        this.baseDefense += defenseBonus;
        this.fullHeal();
    }
}