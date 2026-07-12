package it.unicam.cs.mpgc.rpg125667.util;

/**
 * Classe di utilità che centralizza tutte le costanti di bilanciamento del gioco.
 * <p>
 * Questa classe agisce come unico punto di configurazione per evitare l'uso di 
 * "Magic Numbers" all'interno della logica di business, permettendo un 
 * bilanciamento rapido del gameplay (es. probabilità di schivata, cure, XP) 
 * senza dover modificare il codice sorgente nelle classi di logica.
 * </p>
 */
public final class GameConfig {

    // --- COMBATTIMENTO ---

    /**
     * Soglia (su una scala 0-100, estremi inclusi) sotto la quale un attacco viene
     * considerato schivato dal difensore. Con il valore di default (10) la
     * probabilità di schivata è del 10%.
     */
    public static final int DODGE_THRESHOLD = 10;

    /**
     * Soglia (su una scala 0-100) oltre la quale un attacco viene considerato
     * critico. Con il valore di default (90) la probabilità di colpo critico è
     * del 10%.
     */
    public static final int CRIT_THRESHOLD = 90;

    /** Fattore moltiplicativo applicato al danno base in caso di colpo critico. */
    public static final int CRIT_MULTIPLIER = 2;

    /** Quantità minima di danno garantita da un attacco andato a segno. */
    public static final int MIN_DAMAGE = 1;

    // --- GIOCATORE E PROGRESSIONE ---

    /** Percentuale degli HP massimi ripristinata dall'uso di una pozione (0.30 = 30%). */
    public static final double POTION_HEAL_PERCENTAGE = 0.30;

    /** Numero di pozioni assegnate al giocatore ad ogni level up. */
    public static final int BASE_POTIONS = 3;

    /**
     * Moltiplicatore usato per calcolare l'esperienza necessaria a salire di
     * livello (es. per raggiungere il livello 2 sono richiesti 2 * 50 = 100 XP).
     */
    public static final int LEVEL_UP_XP_MULTIPLIER = 50;

    /** Quantità base di esperienza (XP) ottenuta al termine di una battaglia vinta. */
    public static final int BASE_XP_REWARD = 20;

    // Bonus ricevuti al Level Up

    /** Incremento agli HP massimi del giocatore assegnato ad ogni level up. */
    public static final int HP_BONUS_PER_LEVEL = 20;

    /** Incremento all'attacco del giocatore assegnato ad ogni level up. */
    public static final int ATK_BONUS_PER_LEVEL = 2;

    /** Incremento alla difesa del giocatore assegnato ad ogni level up. */
    public static final int DEF_BONUS_PER_LEVEL = 1;

    // --- CONDIZIONE DI VITTORIA ---

    /** Livello che il giocatore deve raggiungere per vincere la partita. */
    public static final int VICTORY_LEVEL = 5;

    /**
     * Costruttore privato per impedire l'istanziamento di questa classe di utilità.
     */
    private GameConfig() {}
}