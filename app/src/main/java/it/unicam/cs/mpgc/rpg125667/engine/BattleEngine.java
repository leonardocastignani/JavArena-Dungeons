package it.unicam.cs.mpgc.rpg125667.engine;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.engine.action.*;

import lombok.*;

import java.util.*;

/**
 * Motore principale della logica di combattimento.
 * <p>
 * Questa classe coordina lo stato corrente della battaglia tra il {@link Player} e il mostro
 * avversario, delegando l'esecuzione delle singole azioni a {@link CombatAction} e il calcolo
 * delle ricompense di fine battaglia a {@link RewardCalculator}.
 * </p>
 */
@Getter
public class BattleEngine {
    
    private final Player player;
    private final Monster monster;
    private boolean isPlayerTurn;
    private final Random random;
    private final RewardCalculator rewardCalculator;

    /**
     * Costruttore completo, utile per iniettare un Random e un RewardCalculator specifici nei test (Mocking).
     *
     * @param player           Il giocatore.
     * @param monster          Il mostro.
     * @param random           L'istanza del generatore randomico.
     * @param rewardCalculator La strategia di calcolo delle ricompense di fine battaglia.
     */
    public BattleEngine(Player player, Monster monster, Random random, RewardCalculator rewardCalculator) {
        this.player = player;
        this.monster = monster;
        this.isPlayerTurn = true;
        this.random = random;
        this.rewardCalculator = rewardCalculator;
    }

    /**
     * Costruttore di default.
     *
     * @param player  Il giocatore.
     * @param monster Il mostro.
     */
    public BattleEngine(Player player, Monster monster) {
        this(player, monster, new Random(), new DefaultRewardCalculator());
    }

    /**
     * Esegue una specifica azione di combattimento.
     * Disaccoppia la logica di risoluzione dell'attacco dal motore principale.
     *
     * @param attacker Il combattente che agisce.
     * @param defender Il bersaglio dell'azione.
     * @param action   L'azione (Attacco, Cura, Magia) da eseguire.
     * @return Il risultato formattato del turno.
     */
    public TurnResult executeAction(Combatant attacker, Combatant defender, CombatAction action) {
        return action.execute(attacker, defender, this.random);
    }

    /**
     * Verifica se la condizione di fine battaglia è stata raggiunta (almeno uno dei due combattenti è morto).
     *
     * @return true se la battaglia è finita.
     */
    public boolean isBattleOver() {
        return !this.player.isAlive() || !this.monster.isAlive();
    }

    /**
     * Genera un messaggio riassuntivo che decreta il vincitore della battaglia.
     *
     * @return Una stringa indicante la vittoria o la sconfitta.
     */
    public String getBattleResult() {
        if (this.player.isAlive() && !this.monster.isAlive()) {
            return "Vittoria! Hai sconfitto " + this.monster.getName() + "!";
        } else if (!this.player.isAlive() && this.monster.isAlive()) {
            return "Sconfitta... Sei stato ucciso da " + this.monster.getName() + "!";
        }
        return "La battaglia è ancora in corso...";
    }

    /**
     * Calcola e assegna le ricompense al giocatore se vince la battaglia, delegando la logica
     * alla strategia di {@link RewardCalculator} configurata.
     *
     * @return La stringa di log che descrive le ricompense ottenute.
     */
    public String grantRewards() {
        return this.rewardCalculator.grantRewards(this.player, this.monster);
    }
}