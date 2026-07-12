package it.unicam.cs.mpgc.rpg125667.engine.generator;

import lombok.*;

/**
 * Classe modello (DTO) che definisce le proprietà di un template di mostro, così come
 * deserializzato dal file JSON delle risorse tramite {@link it.unicam.cs.mpgc.rpg125667.engine.MonsterLoader}.
 * <p>
 * Include nome e gli intervalli di statistiche (salute, attacco, difesa) usati da un
 * {@link MonsterGenerator} per generare istanze concrete di mostro adeguate al livello
 * del giocatore. Grazie a Lombok ({@link Data}, {@link NoArgsConstructor}, {@link AllArgsConstructor})
 * la classe espone automaticamente getter, setter, costruttore vuoto e costruttore con
 * tutti gli argomenti, necessari alla deserializzazione JSON tramite Jackson.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonsterTemplate {
    /** Nome del mostro, mostrato al giocatore durante la battaglia. */
    private String name;
    /** Livello minimo del giocatore richiesto affinché questo template sia considerato idoneo. */
    private int minLevel;
    /** Valore minimo (incluso) dell'intervallo di salute generabile per questo template. */
    private int minHp;
    /** Valore massimo (incluso) dell'intervallo di salute generabile per questo template. */
    private int maxHp;
    /** Valore minimo (incluso) dell'intervallo di attacco generabile per questo template. */
    private int minAtk;
    /** Valore massimo (incluso) dell'intervallo di attacco generabile per questo template. */
    private int maxAtk;
    /** Valore minimo (incluso) dell'intervallo di difesa generabile per questo template. */
    private int minDef;
    /** Valore massimo (incluso) dell'intervallo di difesa generabile per questo template. */
    private int maxDef;
}