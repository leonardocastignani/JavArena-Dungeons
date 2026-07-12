package it.unicam.cs.mpgc.rpg125667.engine.action;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import java.util.*;

/**
 * Implementazione di {@link CombatAction} che gestisce l'attacco fisico.
 * <p>
 * Calcola il danno basandosi sulle statistiche di attacco del mittente e 
 * la difesa del destinatario.
 * </p>
 */
public class BasicAttackAction implements CombatAction {

    /**
     * {@inheritDoc}
     * <p>
     * Prima estrae un numero casuale in {@code [1, 100]}: se ricade entro
     * {@link GameConfig#DODGE_THRESHOLD} il difensore schiva l'attacco e non subisce danni.
     * Altrimenti il danno grezzo è pari all'attacco base dell'attaccante, moltiplicato per
     * {@link GameConfig#CRIT_MULTIPLIER} se il tiro supera {@link GameConfig#CRIT_THRESHOLD}
     * (colpo critico). Il danno effettivo, già ridotto dalla difesa del bersaglio, è calcolato
     * da {@link Combatant#takeDamage(int)}.
     * </p>
     *
     * @param attacker Il combattente che sferra l'attacco.
     * @param defender Il combattente che subisce (o schiva) l'attacco.
     * @param rng      L'istanza Random usata per determinare schivata e colpo critico.
     * @return Il {@link TurnResult} con il log descrittivo, il danno inflitto e i flag di
     *         critico/schivata.
     */
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