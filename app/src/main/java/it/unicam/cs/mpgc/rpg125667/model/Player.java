package it.unicam.cs.mpgc.rpg125667.model;

import com.fasterxml.jackson.annotation.*;

import it.unicam.cs.mpgc.rpg125667.util.*;

import lombok.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Rappresenta l'eroe controllato dal giocatore.
 * <p>
 * Gestisce l'inventario, il sistema di progressione (esperienza/livello) 
 * e le interazioni di combattimento tramite {@link Combatant}.
 * </p>
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
    private String lastSaveDate;

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
     * Aggiunge esperienza e gestisce il controllo del passaggio di livello.
     * <p>
     * Se l'esperienza accumulata supera la soglia necessaria (calcolata tramite 
     * {@code GameConfig}), il metodo invoca automaticamente {@code levelUp()}
     * finché l'esperienza non è esaurita.
     * </p>
     *
     * @param amount Punti esperienza guadagnati.
     * @return {@code true} se il giocatore ha effettuato almeno un passaggio di livello.
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
     * Esegue il level-up del personaggio.
     * <p>
     * Incrementa il livello, ripristina le pozioni ai valori base e potenzia le statistiche 
     * tramite {@link CharacterStats#upgradeStats}.
     * </p>
     */
    private void levelUp() {
        this.level++;
        this.stats.upgradeStats(GameConfig.HP_BONUS_PER_LEVEL, GameConfig.ATK_BONUS_PER_LEVEL, GameConfig.DEF_BONUS_PER_LEVEL);
        this.potions = GameConfig.BASE_POTIONS;
    }

    /**
     * Aggiorna la data di ultimo salvataggio formattata.
     */
    public void updateSaveDate() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.lastSaveDate = now.format(formatter);
    }

    @Override
    public String toString() {
        return this.name + " (Lvl. " + this.level + ")";
    }
}