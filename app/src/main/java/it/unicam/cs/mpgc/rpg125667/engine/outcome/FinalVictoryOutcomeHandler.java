package it.unicam.cs.mpgc.rpg125667.engine.outcome;

import it.unicam.cs.mpgc.rpg125667.engine.*;

/**
 * Gestisce l'esito di vittoria di partita: il giocatore ha appena raggiunto, per la prima
 * volta, {@link it.unicam.cs.mpgc.rpg125667.util.GameConfig#VICTORY_LEVEL} a barra di
 * esperienza piena (vedi {@link it.unicam.cs.mpgc.rpg125667.model.Player#shouldShowVictoryScreen()}).
 * <p>
 * Marca il traguardo come già mostrato, salva automaticamente i progressi e reindirizza
 * alla schermata celebrativa di vittoria.
 * </p>
 */
public class FinalVictoryOutcomeHandler implements BattleOutcomeHandler {

    @Override
    public boolean appliesTo(BattleEngine engine) {
        return engine.getPlayer().isAlive() && engine.getPlayer().shouldShowVictoryScreen();
    }

    @Override
    public void handle(BattleOutcomeContext context) {
        var player = context.engine().getPlayer();
        context.view().logMessage("Hai raggiunto il Livello " + player.getLevel() + ": la tua leggenda e' completa!");
        player.markVictorySeen();
        player.updateSaveDate();
        context.service().saveProgress(player);
        context.view().transitionToVictory();
    }
}