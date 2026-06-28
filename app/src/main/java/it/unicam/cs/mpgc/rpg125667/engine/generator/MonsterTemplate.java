package it.unicam.cs.mpgc.rpg125667.engine.generator;

import lombok.*;

/**
 * Mappa la struttura del file JSON per la generazione dei mostri.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonsterTemplate {
    private String name;
    private int minLevel;
    private int minHp;
    private int maxHp;
    private int minAtk;
    private int maxAtk;
    private int minDef;
    private int maxDef;
}