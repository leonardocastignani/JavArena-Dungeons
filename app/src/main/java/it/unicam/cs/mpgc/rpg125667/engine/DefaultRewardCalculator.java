package it.unicam.cs.mpgc.rpg125667.engine;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

/**
 * Implementazione di {@link RewardCalculator} che applica la formula di ricompensa standard:
 * esperienza base più un bonus proporzionale alla salute massima del mostro sconfitto.
 */
public class DefaultRewardCalculator implements RewardCalculator {

    /**
     * {@inheritDoc}
     * <p>
     * Non assegna alcuna ricompensa (restituendo una stringa vuota) se il giocatore non è
     * sopravvissuto o se il mostro passato come argomento è ancora in vita, poiché in tal caso
     * la battaglia non può considerarsi vinta. In caso di vittoria, l'esperienza guadagnata è pari
     * a {@link GameConfig#BASE_XP_REWARD} sommata alla metà della salute massima del mostro sconfitto;
     * se tale esperienza fa salire di livello il giocatore, il messaggio di log include anche
     * l'annuncio del level up.
     * </p>
     *
     * @param player          Il giocatore vincitore.
     * @param defeatedMonster Il mostro (presunto) sconfitto.
     * @return La stringa di log che descrive le ricompense ottenute, oppure una stringa vuota
     *         se le condizioni di vittoria non sono soddisfatte.
     */
    @Override
    public String grantRewards(Player player, Monster defeatedMonster) {
        if (!player.isAlive() || defeatedMonster.isAlive()) return "";

        int xpReward = GameConfig.BASE_XP_REWARD + (defeatedMonster.getStats().getMaxHealth() / 2);
        boolean leveledUp = player.gainXp(xpReward);

        StringBuilder sb = new StringBuilder();
        sb.append("Hai ottenuto ").append(xpReward).append(" punti esperienza!");

        if (leveledUp) {
            sb.append("\nSALI DI LIVELLO! Sei ora al Livello ").append(player.getLevel()).append("!");
            sb.append("\nSalute e pozioni ripristinate. Statistiche aumentate!");
        }

        return sb.toString();
    }
}