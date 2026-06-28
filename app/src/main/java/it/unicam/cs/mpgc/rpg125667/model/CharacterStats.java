package it.unicam.cs.mpgc.rpg125667.model;

import lombok.*;

/**
 * Incapsula le statistiche numeriche vitali e di combattimento di un'entità.
 * <p>
 * Questa classe gestisce la logica di calcolo sicura per la variazione della salute
 * (impedendo valori negativi o eccedenze oltre il massimo) e il potenziamento dei parametri.
 * </p>
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
     * Riduce la salute corrente dell'entità.
     * <p>
     * Il valore è garantito non scendere mai sotto lo zero (floor).
     * </p>
     *
     * @param amount La quantità di danni da sottrarre. Deve essere un valore positivo.
     */
    public void reduceHealth(int amount) {
        if (amount > 0) {
            this.currentHealth = Math.max(0, this.currentHealth - amount);
        }
    }

    /**
     * Ripristina la salute corrente dell'entità.
     * <p>
     * Il valore è garantito non superare mai {@code maxHealth} (cap).
     * </p>
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
     * Incrementa permanentemente le statistiche dell'entità.
     * <p>
     * Metodo utilizzato durante l'avanzamento di livello per aggiornare i massimali.
     * </p>
     *
     * @param healthBonus  Incremento ai punti vita massimi.
     * @param atkBonus     Incremento all'attacco base.
     * @param defBonus     Incremento alla difesa base.
     */
    public void upgradeStats(int healthBonus, int attackBonus, int defenseBonus) {
        this.maxHealth += healthBonus;
        this.baseAttack += attackBonus;
        this.baseDefense += defenseBonus;
        this.fullHeal();
    }
}