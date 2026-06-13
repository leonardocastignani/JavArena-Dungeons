package it.unicam.cs.mpgc.rpg125667.model;

import com.fasterxml.jackson.annotation.*;

import it.unicam.cs.mpgc.rpg125667.util.*;

import lombok.*;

import java.util.*;

/**
 * Rappresenta l'eroe controllato dal giocatore all'interno del gioco.
 * Implementa l'interfaccia {@link Combatant} per interagire nel sistema di combattimento.
 * Gestisce l'inventario (pozioni), il sistema di progressione (esperienza e livello) 
 * e le statistiche base tramite composizione con {@link CharacterStats}.
 */
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

    /**
     * Costruisce un nuovo giocatore con il nome e le statistiche fornite.
     * Genera automaticamente un ID univoco per la persistenza dei dati.
     *
     * @param name  Il nome scelto dal giocatore per l'eroe.
     * @param stats Le statistiche iniziali (Salute, Attacco, Difesa) dell'eroe.
     */
    public Player(String name, CharacterStats stats) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.stats = stats;
    }

    /**
     * Restituisce la salute attuale del giocatore (ignorato da Jackson per non duplicare dati).
     *
     * @return I punti vita attuali.
     */
    @Override
    @JsonIgnore
    public int getCurrentHealth() {
        return this.stats.getCurrentHealth();
    }

    /**
     * Controlla se il giocatore è in vita.
     *
     * @return true se il giocatore è vivo.
     */
    @Override
    @JsonIgnore
    public boolean isAlive() {
        return this.stats.getCurrentHealth() > 0;
    }

    /**
     * Gestisce la ricezione dei danni decurtandoli della propria armatura.
     *
     * @param rawDamage Il danno base.
     * @return I danni effettivi subiti.
     */
    @Override
    public int takeDamage(int rawDamage) {
        int actualDamage = Math.max(GameConfig.MIN_DAMAGE, rawDamage - this.stats.getBaseDefense());
        this.stats.reduceHealth(actualDamage);
        return actualDamage;
    }

    /**
     * Tenta di consumare una pozione curativa dall'inventario del giocatore.
     *
     * @return true se la pozione è stata consumata con successo (salute non al massimo e pozioni disponibili),
     * false se il giocatore ha i punti vita massimi o non ha pozioni.
     */
    public boolean usePotion() {
        if (this.potions > 0 && this.stats.getCurrentHealth() < this.stats.getMaxHealth()) {
            this.potions--;
            int healAmount = (int) (this.stats.getMaxHealth() * GameConfig.POTION_HEAL_PERCENTAGE);
            this.stats.heal(healAmount);
            return true;
        }
        return false;
    }

    /**
     * Aggiunge punti esperienza al giocatore e gestisce l'eventuale passaggio di livello
     * basato sul moltiplicatore configurato in {@link it.unicam.cs.mpgc.rpg125667.util.GameConfig}.
     *
     * @param amount La quantità di punti esperienza guadagnati.
     * @return true se l'aggiunta di esperienza ha causato uno o più passaggi di livello, false altrimenti.
     */
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

    /**
     * Gestisce la logica di avanzamento di livello.
     * Incrementa il livello attuale, ripristina le pozioni di base e potenzia le statistiche.
     */
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