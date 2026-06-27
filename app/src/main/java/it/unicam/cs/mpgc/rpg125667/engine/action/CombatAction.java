package it.unicam.cs.mpgc.rpg125667.engine.action;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;

import java.util.*;

/**
 * Rappresenta un'azione generica eseguibile durante un turno di combattimento (Pattern Strategy).
 * Permette al BattleEngine di eseguire attacchi, cure o magie senza conoscerne l'implementazione interna.
 */
public interface CombatAction {
    
    /**
     * Esegue l'azione di combattimento.
     *
     * @param attacker Il combattente che esegue l'azione (sorgente).
     * @param defender Il combattente che subisce l'azione (bersaglio).
     * @param rng      L'istanza Random fornita dall'Engine per calcolare probabilità (critici, schivate).
     * @return Il DTO TurnResult contenente l'esito dell'azione e il log testuale.
     */
    TurnResult execute(Combatant attacker, Combatant defender, Random rng);
}