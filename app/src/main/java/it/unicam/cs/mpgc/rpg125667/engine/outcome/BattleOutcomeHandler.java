package it.unicam.cs.mpgc.rpg125667.engine.outcome;

import it.unicam.cs.mpgc.rpg125667.engine.*;

/**
 * Strategia che riconosce e gestisce un possibile esito di fine battaglia (sconfitta, vittoria
 * di partita, prosecuzione, o qualunque nuova regola di progressione futura).
 * <p>
 * Aggiungere una nuova regola di avanzamento (es. un evento speciale al termine di uno scontro
 * contro un boss, o un nuovo traguardo intermedio) richiede solo di scrivere una nuova classe
 * che implementa questa interfaccia e di registrarla, senza modificare il controller
 * dell'Arena né le altre strategie già esistenti (Open/Closed Principle).
 * </p>
 */
public interface BattleOutcomeHandler {

    /**
     * Verifica se questa strategia si applica allo stato attuale della battaglia.
     *
     * @param engine Il motore della battaglia appena conclusa.
     * @return true se questa strategia è quella responsabile di gestire l'esito corrente.
     */
    boolean appliesTo(BattleEngine engine);

    /**
     * Applica l'esito: aggiorna la persistenza se necessario e riflette il risultato sulla view.
     *
     * @param context Le dipendenze necessarie per gestire l'esito.
     */
    void handle(BattleOutcomeContext context);
}