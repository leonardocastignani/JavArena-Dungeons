package it.unicam.cs.mpgc.rpg125667.engine.generator;

import it.unicam.cs.mpgc.rpg125667.model.*;

import lombok.extern.slf4j.*;

import java.util.*;
import java.util.stream.*;

/**
 * Implementazione di {@link MonsterGenerator} che crea nemici casuali.
 * <p>
 * Utilizza un generatore di numeri casuali per variare le statistiche e il tipo 
 * di mostro generato in base al livello del giocatore.
 * </p>
 */
@Slf4j
public class RandomMonsterGenerator implements MonsterGenerator {

    private final List<MonsterTemplate> templates;
    private final Random random = new Random();

    /**
     * Crea un generatore che pesca casualmente da un pool di template dati.
     *
     * @param templates La lista dei {@link MonsterTemplate} disponibili, tipicamente ottenuta
     *                   da {@link it.unicam.cs.mpgc.rpg125667.engine.MonsterLoader#getTemplates()}.
     */
    public RandomMonsterGenerator(List<MonsterTemplate> templates) {
        this.templates = templates;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Seleziona fino a 3 template idonei (con {@code minLevel <= playerLevel}), privilegiando
     * quelli con il livello minimo più alto tra gli idonei, e ne sceglie uno casualmente. Se
     * nessun template risulta idoneo al livello richiesto, ricade sull'intero pool disponibile
     * registrando un avviso nel log. Le statistiche del mostro generato (salute, attacco,
     * difesa) sono estratte casualmente dagli intervalli {@code [min, max]} del template scelto
     * e scalate applicando un moltiplicatore crescente linearmente con il livello del giocatore
     * (+20% per ogni livello oltre il primo).
     * </p>
     *
     * @param playerLevel Livello attuale del giocatore.
     * @return Una nuova istanza di {@link Monster} con statistiche scalate al livello richiesto.
     * @throws IllegalArgumentException se il pool di template (sia quello idoneo sia, in
     *                                   fallback, quello completo) è vuoto, poiché in tal caso
     *                                   {@link Random#nextInt(int)} riceve un limite non positivo.
     */
    @Override
    public Monster generate(int playerLevel) {
        List<MonsterTemplate> suitableMonsters = this.templates.stream()
                .filter(t -> t != null && t.getMinLevel() <= playerLevel)
                .sorted((m1, m2) -> Integer.compare(m2.getMinLevel(), m1.getMinLevel()))
                .limit(3) 
                .collect(Collectors.toList());

        if (suitableMonsters.isEmpty()) {
            log.warn("Nessun mostro adatto al livello {}. Uso il pool completo.", playerLevel);
            suitableMonsters = this.templates;
        }

        MonsterTemplate t = suitableMonsters.get(random.nextInt(suitableMonsters.size()));

        double multiplier = 1.0 + ((playerLevel - 1) * 0.2);
        int hp = (int) ((this.random.nextInt((t.getMaxHp() - t.getMinHp()) + 1) + t.getMinHp()) * multiplier);
        int attack = (int) ((this.random.nextInt((t.getMaxAtk() - t.getMinAtk()) + 1) + t.getMinAtk()) * multiplier);
        int defense = (int) ((this.random.nextInt((t.getMaxDef() - t.getMinDef()) + 1) + t.getMinDef()) * multiplier);

        CharacterStats stats = new CharacterStats(hp, hp, attack, defense);
        return new Monster(t.getName(), stats);
    }
}