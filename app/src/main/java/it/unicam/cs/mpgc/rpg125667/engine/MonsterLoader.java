package it.unicam.cs.mpgc.rpg125667.engine;

import com.fasterxml.jackson.databind.*;

import it.unicam.cs.mpgc.rpg125667.engine.generator.*;

import lombok.extern.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * Responsabile ESCLUSIVAMENTE del caricamento I/O dei dati dei mostri dal disco.
 * Fornisce il database in memoria ai vari Generator.
 * <p>
 * Il caricamento avviene leggendo il file JSON delle risorse
 * {@code /it/unicam/cs/mpgc/rpg125667/data/monsters.json} e deserializzandolo in una lista
 * di {@link MonsterTemplate}, mantenuta in un campo statico condiviso da tutta l'applicazione.
 * </p>
 */
@Slf4j
public class MonsterLoader {

    private static List<MonsterTemplate> templates = Collections.emptyList();

    /**
     * Carica il file JSON dei template dei mostri dalle risorse dell'applicazione e popola
     * il database statico in memoria, sovrascrivendo eventuali template caricati in precedenza.
     * <p>
     * Deve essere invocato una volta all'avvio del gioco, prima di qualsiasi tentativo
     * di generazione di mostri tramite {@link it.unicam.cs.mpgc.rpg125667.engine.generator.MonsterGenerator}.
     * </p>
     *
     * @throws RuntimeException se il file delle risorse non viene trovato o non è deserializzabile
     *                           correttamente (dati corrotti, formato JSON non valido, ecc.).
     */
    public static void loadMonsters() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = MonsterLoader.class.getResourceAsStream("/it/unicam/cs/mpgc/rpg125667/data/monsters.json");
            if (is == null) {
                throw new FileNotFoundException("File monsters.json non trovato nelle risorse!");
            }
            MonsterTemplate[] loadedArray = mapper.readValue(is, MonsterTemplate[].class);
            templates = Arrays.asList(loadedArray);
            log.info("Caricati {} template mostri con successo dal DB.", templates.size());
        } catch (Exception e) {
            log.error("Errore critico durante il caricamento dei mostri: {}", e.getMessage());
            throw new RuntimeException("Impossibile avviare il gioco: file mostri corrotto o mancante.", e);
        }
    }

    /**
     * @return La lista immutabile dei template caricati in memoria.
     */
    public static List<MonsterTemplate> getTemplates() {
        return Collections.unmodifiableList(templates);
    }
}