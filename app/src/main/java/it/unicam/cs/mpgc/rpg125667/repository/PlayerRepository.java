package it.unicam.cs.mpgc.rpg125667.repository;

import it.unicam.cs.mpgc.rpg125667.model.*;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;

import java.io.*;
import java.util.*;

public class PlayerRepository {
    
    private final File saveFile = new File("data/saves/savegame.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Player> findAll() {
        if (!this.saveFile.exists()) return new ArrayList<Player>();

        try {
            return this.mapper.readValue(this.saveFile, new TypeReference<List<Player>>() {});
        } catch (IOException e) {
            System.err.println("Errore durante la lettura dei salvataggi: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void save(Player player) {
        List<Player> players = this.findAll();
        
        players.removeIf(p -> p.getId() != null && p.getId().equals(player.getId()));
        players.add(player);

        try {
            this.saveFile.getParentFile().mkdirs();
            this.mapper.writerWithDefaultPrettyPrinter().writeValue(this.saveFile, players);
            System.out.println("Salvataggio aggiornato nel file JSON multiplo!");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    public Optional<Player> findById(String id) {
        return findAll().stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst();
    }

    public void delete(Player player) {
        List<Player> players = new ArrayList<Player>(this.findAll());

        players.removeIf(p -> p.getId() != null && p.getId().equals(player.getId()));

        try {
            this.mapper.writerWithDefaultPrettyPrinter().writeValue(this.saveFile, players);
            System.out.println("L'eroe è caduto. Salvataggio eliminato per sempre.");
        } catch (IOException e) {
            System.err.println("Errore durante l'eliminazione: " + e.getMessage());
        }
    }

    public void close() {}
}