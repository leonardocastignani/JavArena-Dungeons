package it.unicam.cs.mpgc.rpg125667.engine;

import com.fasterxml.jackson.databind.*;

import it.unicam.cs.mpgc.rpg125667.engine.generator.*;

import lombok.extern.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * Responsabile ESCLUSIVAMENTE del caricamento I/O dei dati dei mostri dal disco.
 * Fornisce il database in memoria ai vari Generator.
 */
@Slf4j
public class MonsterLoader {

    private static List<MonsterTemplate> templates = Collections.emptyList();

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