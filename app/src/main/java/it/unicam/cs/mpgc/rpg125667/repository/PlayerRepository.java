package it.unicam.cs.mpgc.rpg125667.repository;

import it.unicam.cs.mpgc.rpg125667.model.*;
import com.fasterxml.jackson.databind.*;
import java.io.*;
import java.util.*;

public class PlayerRepository {
    
    private final File saveFile = new File("src/data/savegame.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public void save(Player player) {
        try {
            this.saveFile.getParentFile().mkdirs();
            this.mapper.writerWithDefaultPrettyPrinter().writeValue(this.saveFile, player);
            System.out.println("Partita salvata con successo in JSON!");
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    public Optional<Player> findById(Long id) {
        if (!this.saveFile.exists()) return Optional.empty();

        try {
            Player loadedPlayer = this.mapper.readValue(this.saveFile, Player.class);
            System.out.println("Salvataggio caricato con successo!");
            return Optional.ofNullable(loadedPlayer);
            
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento: " + e.getMessage());
            return Optional.empty();
        }
    }

    public void close() {}
}