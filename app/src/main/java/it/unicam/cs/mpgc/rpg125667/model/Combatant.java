package it.unicam.cs.mpgc.rpg125667.model;

public interface Combatant {
    
    String getName();
    
    int getCurrentHealth();
    
    boolean isAlive();
    
    int takeDamage(int rawDamage);

    CharacterStats getStats();
}