package it.unicam.cs.mpgc.rpg125667.engine.action;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;

import java.util.*;

/**
 * Azione che permette a un combattente (attualmente solo il Player) di consumare
 * una pozione per ripristinare la salute, saltando la fase offensiva.
 */
public class HealAction implements CombatAction {

    @Override
    public TurnResult execute(Combatant attacker, Combatant defender, Random rng) {
        if (attacker instanceof Player player) {
            if (player.usePotion()) {
                return new TurnResult(player.getName() + " beve una pozione curativa e si ristora!", 0, false, false);
            } else {
                return new TurnResult(player.getName() + " cerca pozioni nello zaino, ma è vuoto (o la salute è al massimo)!", 0, false, false);
            }
        }
        return new TurnResult(attacker.getName() + " cerca di curarsi ma non ha oggetti utili.", 0, false, false);
    }
}