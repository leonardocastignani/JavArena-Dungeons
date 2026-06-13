package it.unicam.cs.mpgc.rpg125667.util;

/**
 * Classe di utilità che centralizza tutte le costanti di bilanciamento del gioco.
 * Previene l'utilizzo di "Magic Numbers" all'interno della logica di business,
 * facilitando future modifiche al bilanciamento delle meccaniche.
 */
public final class GameConfig {
    
    // --- COMBATTIMENTO ---
    public static final int DODGE_THRESHOLD = 10; // Fino a 10 su 100 (10% Schivata)
    public static final int CRIT_THRESHOLD = 90;  // Oltre 90 su 100 (10% Critico)
    public static final int CRIT_MULTIPLIER = 2;  // Il danno raddoppia
    public static final int MIN_DAMAGE = 1;       // Danno minimo garantito

    // --- GIOCATORE E PROGRESSIONE ---
    public static final double POTION_HEAL_PERCENTAGE = 0.30; // Cura il 30% degli HP Massimi
    public static final int BASE_POTIONS = 3;                 // Pozioni resettate al level up
    public static final int LEVEL_UP_XP_MULTIPLIER = 50;      // Es. Livello 2 richiede 100 XP
    public static final int BASE_XP_REWARD = 20;              // XP base ottenuti a fine battaglia
    
    // Bonus ricevuti al Level Up
    public static final int HP_BONUS_PER_LEVEL = 20;
    public static final int ATK_BONUS_PER_LEVEL = 2;
    public static final int DEF_BONUS_PER_LEVEL = 1;

    // --- CONDIZIONE DI VITTORIA ---
    public static final int VICTORY_LEVEL = 5;

    /**
     * Costruttore privato per impedire l'istanziamento di questa classe di utilità
     */
    private GameConfig() {}
}