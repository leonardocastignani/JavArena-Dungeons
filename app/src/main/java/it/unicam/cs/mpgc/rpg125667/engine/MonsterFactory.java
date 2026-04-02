package it.unicam.cs.mpgc.rpg125667.engine;

import it.unicam.cs.mpgc.rpg125667.model.*;
import java.util.*;

public class MonsterFactory {

    private static final Random random = new Random();

    private static final String[] MONSTER_NAMES = {
        "Goblin Infuriato", 
        "Orco Crudele", 
        "Scheletro Guerriero", 
        "Lupo Mannaro",
        "Bandito della Via"
    };

    public static Monster generateRandomMonster() {
        String randomName = MONSTER_NAMES[random.nextInt(MONSTER_NAMES.length)];

        int hp = random.nextInt(41) + 40;
        int attack = random.nextInt(7) + 8;
        int defense = random.nextInt(4) + 1; 

        CharacterStats stats = new CharacterStats(hp, hp, attack, defense);
        return new Monster(randomName, stats);
    }
}