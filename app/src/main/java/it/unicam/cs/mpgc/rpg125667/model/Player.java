package it.unicam.cs.mpgc.rpg125667.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class Player implements Combatant {

    private String id;
    private String name;
    private CharacterStats stats;
    private int potions = 3;

    public Player(String name, CharacterStats stats) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.stats = stats;
    }

    @Override
    @JsonIgnore
    public int getCurrentHealth() {
        return this.stats.getCurrentHealth();
    }

    @Override
    @JsonIgnore
    public boolean isAlive() {
        return this.stats.getCurrentHealth() > 0;
    }

    @Override
    public void takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - this.stats.getBaseDefense());
        this.stats.reduceHealth(actualDamage);
        System.out.println(this.name + " subisce " + actualDamage + " danni! Salute rimanente: " + this.stats.getCurrentHealth());
    }

    public void attack(Combatant target) {
        System.out.println(this.name + " attacca " + target.getName() + "!");
        target.takeDamage(this.stats.getBaseAttack());
    }

    public boolean usePotion() {
        if (this.potions > 0) {
            this.potions--;
            this.stats.heal(30); 
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.name + " (Attacco: " + this.stats.getBaseAttack() + " | Difesa: " + this.stats.getBaseDefense() + ")";
    }
}