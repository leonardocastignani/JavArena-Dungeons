package it.unicam.cs.mpgc.rpg125667.repository;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;

import it.unicam.cs.mpgc.rpg125667.model.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class JsonPlayerRepository implements IPlayerRepository {

    private final File saveFile = new File("data/saves/savegame.json");
    private final ObjectMapper mapper = new ObjectMapper();
    
    private List<Player> cachedPlayers = new CopyOnWriteArrayList<Player>();

    @Override
    public List<Player> findAll() {
        if (!this.cachedPlayers.isEmpty()) {
            return this.cachedPlayers;
        }

        if (!this.saveFile.exists()) {
            return this.cachedPlayers;
        }

        try {
            List<Player> loaded = this.mapper.readValue(this.saveFile, new TypeReference<List<Player>>() {});
            this.cachedPlayers.addAll(loaded);
        } catch (Exception e) {
            System.err.println("Errore lettura DB: " + e.getMessage());
        }
        return this.cachedPlayers;
    }

    @Override
    public void save(Player player) {
        List<Player> players = this.findAll();
        players.removeIf(p -> p.getId() != null && p.getId().equals(player.getId()));
        players.add(player);
        
        this.flushAsync(new ArrayList<Player>(players));
    }

    @Override
    public void delete(Player player) {
        List<Player> players = this.findAll();
        players.removeIf(p -> p.getId() != null && p.getId().equals(player.getId()));
        this.flushAsync(new ArrayList<Player>(players));
    }

    @Override
    public void close() {}

    private void flushAsync(List<Player> snapshot) {
        CompletableFuture.runAsync(() -> {
            File tempFile = new File(this.saveFile.getAbsolutePath() + ".tmp");
            try {
                this.saveFile.getParentFile().mkdirs();
                this.mapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, snapshot);
                Files.move(tempFile.toPath(), this.saveFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
                System.out.println("[ASYNC] DB JSON aggiornato in background.");
            } catch (IOException e) {
                System.err.println("[ASYNC] Errore critico I/O: " + e.getMessage());
                if (tempFile.exists()) tempFile.delete();
            }
        });
    }
}