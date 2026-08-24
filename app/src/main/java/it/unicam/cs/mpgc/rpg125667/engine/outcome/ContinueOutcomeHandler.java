package it.unicam.cs.mpgc.rpg125667.engine.outcome;

import it.unicam.cs.mpgc.rpg125667.engine.*;

/**
 * Gestisce l'esito di default: il giocatore ha vinto la battaglia ma non ha (ancora)
 * raggiunto la vittoria di partita.
 * <p>
 * Va registrata per ultima tra i {@link BattleOutcomeHandler} configurati, poiché si applica
 * a qualunque vittoria non già intercettata da una strategia più specifica.
 * </p>
 */
public class ContinueOutcomeHandler implements BattleOutcomeHandler {

    @Override
    public boolean appliesTo(BattleEngine engine) {
        return engine.getPlayer().isAlive();
    }

    @Override
    public void handle(BattleOutcomeContext context) {
        var player = context.engine().getPlayer();
        context.view().enableContinuation(
                "La battaglia e' terminata. Usa 'Prossima Battaglia' per continuare, oppure 'Salva Partita' per mettere al sicuro i progressi!\nSalute rimanente: "
                        + player.getCurrentHealth() + " HP.");
    }
}