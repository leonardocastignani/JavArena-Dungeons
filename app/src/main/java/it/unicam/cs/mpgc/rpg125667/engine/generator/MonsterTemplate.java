package it.unicam.cs.mpgc.rpg125667.engine.generator;

import lombok.*;

/**
 * Classe modello che definisce le proprietà di un mostro.
 * <p>
 * Include nome, statistiche di base e parametri necessari per l'inizializzazione 
 * nell'engine di gioco.
 * </p>
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