package it.unicam.cs.mpgc.rpg125667.engine;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import lombok.*;

import java.util.*;

/**
 * Motore di calcolo deterministico per il sistema di combattimento.
 * Agisce come arbitro neutrale: calcola i tiri di dado (RNG), i colpi critici 
 * e le schivate, passando poi l'attacco "grezzo" ai difensori che applicheranno 
 * autonomamente le proprie statistiche difensive.
 */
@Getter
public class BattleEngine {
    
    private final Player player;
    private final Monster monster;
    private boolean isPlayerTurn;
    private final Random random;

    /**
     * Costruttore completo, utile per iniettare un Random specifico nei test (Mocking).
     *
     * @param player  Il giocatore.
     * @param monster Il mostro.
     * @param random  L'istanza del generatore randomico.
     */
    public BattleEngine(Player player, Monster monster, Random random) {
        this.player = player;
        this.monster = monster;
        this.isPlayerTurn = true;
        this.random = random;
    }

    /**
     * Costruttore di default.
     *
     * @param player  Il giocatore.
     * @param monster Il mostro.
     */
    public BattleEngine(Player player, Monster monster) {
        this(player, monster, new Random());
    }

    /**
     * Esegue il turno di attacco del giocatore contro il mostro.
     *
     * @return Il {@link TurnResult} contenente i dettagli dell'azione.
     */
    public TurnResult executePlayerAttack() {
        int attackRoll = this.random.nextInt(100) + 1;

        if (attackRoll <= GameConfig.DODGE_THRESHOLD) {
            String log = "SCHIVATA! " + this.monster.getName() + " evita agilmente l'attacco di " + this.player.getName() + "!";
            return new TurnResult(log, 0, false, true);
        }

        int rawDamage = this.player.getStats().getBaseAttack();
        boolean isCrit = (attackRoll > GameConfig.CRIT_THRESHOLD);
        if (isCrit) {
            rawDamage *= GameConfig.CRIT_MULTIPLIER;
        }

        int actualDamage = this.monster.takeDamage(rawDamage);

        String log;
        if (isCrit) {
            log = "CRITICO! " + this.player.getName() + " sferra un colpo micidiale e infligge " + actualDamage + " danni!";
        } else {
            log = this.player.getName() + " attacca e infligge " + actualDamage + " danni.";
        }
        
        return new TurnResult(log, actualDamage, isCrit, false);
    }

    /**
     * Esegue il turno di contrattacco del mostro contro il giocatore.
     *
     * @return Il {@link TurnResult} contenente i dettagli dell'azione.
     */
    public TurnResult executeMonsterAttack() {
        int attackRoll = this.random.nextInt(100) + 1;

        if (attackRoll <= GameConfig.DODGE_THRESHOLD) {
            String log = "SCHIVATA! " + this.player.getName() + " evita agilmente l'attacco di " + this.monster.getName() + "!";
            return new TurnResult(log, 0, false, true);
        }

        int rawDamage = this.monster.getStats().getBaseAttack();
        boolean isCrit = (attackRoll > GameConfig.CRIT_THRESHOLD);
        if (isCrit) {
            rawDamage *= GameConfig.CRIT_MULTIPLIER;
        }

        int actualDamage = this.player.takeDamage(rawDamage);

        String log;
        if (isCrit) {
            log = "CRITICO! " + this.monster.getName() + " sferra un colpo micidiale e infligge " + actualDamage + " danni!";
        } else {
            log = this.monster.getName() + " attacca e infligge " + actualDamage + " danni.";
        }

        return new TurnResult(log, actualDamage, isCrit, false);
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
     * Calcola e assegna i punti esperienza al giocatore se vince la battaglia.
     * Ritorna anche le informazioni testuali su eventuali "Level Up".
     *
     * @return La stringa di log che descrive le ricompense ottenute.
     */
    public String grantRewards() {
        if (!this.player.isAlive() || this.monster.isAlive()) return "";

        int xpReward = GameConfig.BASE_XP_REWARD + (this.monster.getStats().getMaxHealth() / 2);
        boolean leveledUp = this.player.gainXp(xpReward);

        StringBuilder sb = new StringBuilder();
        sb.append("Hai ottenuto ").append(xpReward).append(" punti esperienza!");
        
        if (leveledUp) {
            sb.append("\nSALI DI LIVELLO! Sei ora al Livello ").append(this.player.getLevel()).append("!");
            sb.append("\nSalute e pozioni ripristinate. Statistiche aumentate!");
        }

        return sb.toString();
    }
}