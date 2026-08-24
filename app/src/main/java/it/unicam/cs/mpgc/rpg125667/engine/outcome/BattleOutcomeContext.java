package it.unicam.cs.mpgc.rpg125667.engine.outcome;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.service.*;

/**
 * Raggruppa le dipendenze di cui un {@link BattleOutcomeHandler} ha bisogno per applicare
 * il proprio esito: lo stato della battaglia appena conclusa, il servizio di persistenza
 * e la view su cui riflettere il risultato.
 *
 * @param engine  Il motore della battaglia appena conclusa.
 * @param service Il servizio di gioco, usato per salvare o eliminare i progressi.
 * @param view    La view su cui applicare l'esito (log, transizioni di scena, UI di prosecuzione).
 */
public record BattleOutcomeContext(BattleEngine engine, GameService service, BattleOutcomeView view) {
}