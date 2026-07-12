package it.unicam.cs.mpgc.rpg125667.engine;

import it.unicam.cs.mpgc.rpg125667.model.*;

/**
 * Interfaccia che definisce il contratto per il calcolo delle ricompense di fine battaglia.
 * Permette di avere implementazioni diverse (Es. ricompense standard, bonus boss, modalità difficile).
 */
public interface RewardCalculator {

    /**
     * Calcola ed assegna le ricompense al giocatore vincitore, gestendo anche l'eventuale Level Up.
     *
     * @param player           Il giocatore vincitore.
     * @param defeatedMonster  Il mostro sconfitto.
     * @return La stringa di log che descrive le ricompense ottenute.
     */
    String grantRewards(Player player, Monster defeatedMonster);
}