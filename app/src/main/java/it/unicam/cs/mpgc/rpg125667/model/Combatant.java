package it.unicam.cs.mpgc.rpg125667.model;

public interface Combatant {
    
    String getName();
    
    int getCurrentHealth();
    
    boolean isAlive();
    
    void takeDamage(int damage);

    void attack(Combatant target);
}