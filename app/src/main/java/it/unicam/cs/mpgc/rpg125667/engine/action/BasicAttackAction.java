package it.unicam.cs.mpgc.rpg125667.engine.action;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import java.util.*;

/**
 * Implementazione di un attacco fisico base.
 * Calcola probabilità di schivata, colpo critico e applica il danno al difensore.
 */
public class BasicAttackAction implements CombatAction {

    @Override
    public TurnResult execute(Combatant attacker, Combatant defender, Random rng) {
        int attackRoll = rng.nextInt(100) + 1;

        if (attackRoll <= GameConfig.DODGE_THRESHOLD) {
            String log = "SCHIVATA! " + defender.getName() + " evita agilmente l'attacco di " + attacker.getName() + "!";
            return new TurnResult(log, 0, false, true);
        }

        int rawDamage = attacker.getStats().getBaseAttack();
        boolean isCrit = (attackRoll > GameConfig.CRIT_THRESHOLD);
        if (isCrit) {
            rawDamage *= GameConfig.CRIT_MULTIPLIER;
        }

        int actualDamage = defender.takeDamage(rawDamage);

        String log;
        if (isCrit) {
            log = "CRITICO! " + attacker.getName() + " sferra un colpo micidiale e infligge " + actualDamage + " danni!";
        } else {
            log = attacker.getName() + " attacca e infligge " + actualDamage + " danni.";
        }

        return new TurnResult(log, actualDamage, isCrit, false);
    }
}