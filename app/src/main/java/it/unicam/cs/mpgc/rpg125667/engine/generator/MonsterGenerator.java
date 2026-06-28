package it.unicam.cs.mpgc.rpg125667.engine.generator;

import it.unicam.cs.mpgc.rpg125667.model.*;

/**
 * Interfaccia che definisce il contratto per la generazione dei mostri.
 * Permette di avere implementazioni diverse (Es. Random, Boss, Difficoltà fissa).
 */
public interface MonsterGenerator {
    
    /**
     * Genera un mostro adatto al livello del giocatore.
     *
     * @param playerLevel Livello attuale del giocatore.
     * @return L'entità Mostro pronta per la battaglia.
     */
    Monster generate(int playerLevel);
}