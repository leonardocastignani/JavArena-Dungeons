package it.unicam.cs.mpgc.rpg125667.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Player implements Combatant {

    private Long id = 1L;
    private String name;
    private CharacterStats stats;

    public Player(String name, CharacterStats stats) {
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
    public void takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - this.stats.getBaseDefense());
        this.stats.reduceHealth(actualDamage);
        System.out.println(this.name + " subisce " + actualDamage + " danni! Salute rimanente: " + this.stats.getCurrentHealth());
    }

    public void attack(Combatant target) {
        System.out.println(this.name + " attacca " + target.getName() + "!");
        target.takeDamage(this.stats.getBaseAttack());
    }
}