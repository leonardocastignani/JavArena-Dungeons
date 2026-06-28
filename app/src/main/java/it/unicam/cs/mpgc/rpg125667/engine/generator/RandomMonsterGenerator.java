package it.unicam.cs.mpgc.rpg125667.engine.generator;

import it.unicam.cs.mpgc.rpg125667.model.*;

import lombok.extern.slf4j.*;

import java.util.*;
import java.util.stream.*;

/**
 * Implementazione di MonsterGenerator che seleziona un mostro casuale,
 * filtrando tramite Stream API solo quelli adatti al livello del giocatore,
 * e applicando uno scaling procedurale delle statistiche.
 */
@Slf4j
public class RandomMonsterGenerator implements MonsterGenerator {

    private final List<MonsterTemplate> templates;
    private final Random random = new Random();

    public RandomMonsterGenerator(List<MonsterTemplate> templates) {
        this.templates = templates;
    }

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