package it.unicam.cs.mpgc.rpg125667.engine;

import com.fasterxml.jackson.databind.*;

import it.unicam.cs.mpgc.rpg125667.model.*;

import lombok.*;

import java.io.*;
import java.util.*;

public class MonsterFactory {

    private static final Random random = new Random();
    private static List<MonsterTemplate> templates;

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = MonsterFactory.class.getResourceAsStream("/it/unicam/cs/mpgc/rpg125667/data/monsters.json");
            if (is == null) {
                throw new RuntimeException("File monsters.json non trovato nelle risorse!");
            }
            MonsterTemplate[] loadedArray = mapper.readValue(is, MonsterTemplate[].class);
            templates = Arrays.asList(loadedArray);
        } catch (Exception e) {
            System.err.println("Errore critico durante il caricamento dei mostri: " + e.getMessage());
            templates = List.of(new MonsterTemplate("Mostro Buggato", 10, 10, 1, 1, 0, 0));
        }
    }

    public static Monster generateRandomMonster() {
        MonsterTemplate t = templates.get(random.nextInt(templates.size()));

        int hp = random.nextInt((t.maxHp - t.minHp) + 1) + t.minHp;
        int attack = random.nextInt((t.maxAtk - t.minAtk) + 1) + t.minAtk;
        int defense = random.nextInt((t.maxDef - t.minDef) + 1) + t.minDef;

        CharacterStats stats = new CharacterStats(hp, hp, attack, defense);
        return new Monster(t.name, stats);
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