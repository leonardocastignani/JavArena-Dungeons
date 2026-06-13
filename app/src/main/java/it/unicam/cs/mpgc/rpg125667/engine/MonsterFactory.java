package it.unicam.cs.mpgc.rpg125667.engine;

import com.fasterxml.jackson.databind.*;

import it.unicam.cs.mpgc.rpg125667.model.*;

import lombok.*;
import lombok.extern.slf4j.*;

import java.io.*;
import java.util.*;

@Slf4j
public class MonsterFactory {

    private static final Random random = new Random();
    private static List<MonsterTemplate> templates;

    public static void loadMonsters() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = MonsterFactory.class.getResourceAsStream("/it/unicam/cs/mpgc/rpg125667/data/monsters.json");
            if (is == null) {
                throw new FileNotFoundException("File monsters.json non trovato nelle risorse!");
            }
            MonsterTemplate[] loadedArray = mapper.readValue(is, MonsterTemplate[].class);
            templates = Arrays.asList(loadedArray);
            log.info("Caricati {} template mostri con successo.", templates.size());
        } catch (Exception e) {
            log.error("Errore critico durante il caricamento dei mostri: {}", e.getMessage());
            throw new RuntimeException("Impossibile avviare il gioco: file mostri corrotto o mancante.", e);
        }
    }

    public static Monster generateRandomMonster(int playerLevel) {
        MonsterTemplate t = templates.get(random.nextInt(templates.size()));

        double multiplier = 1.0 + ((playerLevel - 1) * 0.2);

        int hp = (int) ((random.nextInt((t.getMaxHp() - t.getMinHp()) + 1) + t.getMinHp()) * multiplier);
        int attack = (int) ((random.nextInt((t.getMaxAtk() - t.getMinAtk()) + 1) + t.getMinAtk()) * multiplier);
        int defense = (int) ((random.nextInt((t.getMaxDef() - t.getMinDef()) + 1) + t.getMinDef()) * multiplier);

        CharacterStats stats = new CharacterStats(hp, hp, attack, defense);
        return new Monster(t.getName(), stats);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MonsterTemplate {
        private String name;
        private int minHp;
        private int maxHp;
        private int minAtk;
        private int maxAtk;
        private int minDef;
        private int maxDef;
    }
}