package it.unicam.cs.mpgc.rpg125667.model;

import it.unicam.cs.mpgc.rpg125667.util.*;

import lombok.*;

/**
 * Rappresenta un'entità nemica gestita dal computer.
 * <p>
 * Implementa {@link Combatant} per essere compatibile con il {@code BattleEngine}.
 * Utilizza la composizione con {@link CharacterStats} per gestire i dati statistici.
 * </p>
 */
@Getter
@Setter(AccessLevel.PRIVATE)
public class Monster implements Combatant {
    
    private final String name;
    private final CharacterStats stats;

    /**
     * Costruisce un nuovo mostro.
     *
     * @param name  Il nome del mostro.
     * @param stats Le statistiche del mostro.
     */
    public Monster(String name, CharacterStats stats) {
        this.name = name;
        this.stats = stats;
    }

    /**
     * Recupera la salute corrente del mostro.
     *
     * @return La salute attuale.
     */
    @Override
    public int getCurrentHealth() {
        return this.stats.getCurrentHealth();
    }

    /**
     * Verifica se il mostro è ancora in vita.
     *
     * @return true se la salute è > 0, false altrimenti.
     */
    @Override
    public boolean isAlive() {
        return this.stats.getCurrentHealth() > 0;
    }

    /**
     * Calcola il danno finale sottraendo la difesa del mostro dal danno grezzo in arrivo
     * e aggiorna la salute corrente del mostro di conseguenza.
     * <p>
     * Il danno effettivo non scende mai sotto {@code GameConfig.MIN_DAMAGE}, anche
     * qualora la difesa del mostro sia superiore o uguale al danno grezzo ricevuto.
     * </p>
     *
     * @param rawDamage Il danno grezzo in arrivo, prima della mitigazione da difesa.
     * @return Il danno effettivo subito, dopo la mitigazione.
     */
    @Override
    public int takeDamage(int rawDamage) {
        int actualDamage = Math.max(GameConfig.MIN_DAMAGE, rawDamage - this.stats.getBaseDefense());
        this.stats.reduceHealth(actualDamage);
        return actualDamage;
    }
}