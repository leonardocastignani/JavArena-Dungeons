package it.unicam.cs.mpgc.rpg125667.model;

import it.unicam.cs.mpgc.rpg125667.util.*;

import lombok.*;

@Getter
@Setter(AccessLevel.PRIVATE)
public class Monster implements Combatant {
    
    private final String name;
    private final CharacterStats stats;

    public Monster(String name, CharacterStats stats) {
        this.name = name;
        this.stats = stats;
    }

    @Override
    public int getCurrentHealth() {
        return this.stats.getCurrentHealth();
    }

    @Override
    public boolean isAlive() {
        return this.stats.getCurrentHealth() > 0;
    }

    @Override
    public int takeDamage(int rawDamage) {
        int actualDamage = Math.max(GameConfig.MIN_DAMAGE, rawDamage - this.stats.getBaseDefense());
        this.stats.reduceHealth(actualDamage);
        return actualDamage;
    }
}