package it.unicam.cs.mpgc.rpg125667.engine.outcome;

import it.unicam.cs.mpgc.rpg125667.engine.*;

/**
 * Gestisce l'esito di sconfitta: il giocatore è morto in battaglia.
 * <p>
 * Applica la regola del permadeath eliminando in modo permanente il salvataggio del
 * giocatore, quindi reindirizza alla schermata di Game Over.
 * </p>
 */
public class DefeatOutcomeHandler implements BattleOutcomeHandler {

    @Override
    public boolean appliesTo(BattleEngine engine) {
        return !engine.getPlayer().isAlive();
    }

    @Override
    public void handle(BattleOutcomeContext context) {
        context.view().logMessage("Sei morto... I tuoi progressi non verranno salvati.");
        context.service().deleteProgress(context.engine().getPlayer());
        context.view().transitionToDefeat();
    }
}