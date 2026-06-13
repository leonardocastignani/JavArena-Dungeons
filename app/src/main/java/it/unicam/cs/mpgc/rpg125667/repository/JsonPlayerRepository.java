package it.unicam.cs.mpgc.rpg125667.repository;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;

import it.unicam.cs.mpgc.rpg125667.model.*;

import lombok.extern.slf4j.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
public class JsonPlayerRepository implements IPlayerRepository {

    private final File saveFile = new File("data/saves/savegame.json");
    private final ObjectMapper mapper = new ObjectMapper();
    
    private final Map<String, Player> cachedPlayers = new ConcurrentHashMap<String, Player>();
    
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    public JsonPlayerRepository() {
        this.loadCache();
    }

    private void loadCache() {
        if (!this.saveFile.exists()) return;
        try {
            List<Player> loaded = this.mapper.readValue(this.saveFile, new TypeReference<List<Player>>() {});
            for (Player p : loaded) {
                this.cachedPlayers.put(p.getId(), p);
            }
        } catch (Exception e) {
            log.error("Errore lettura DB: {}", e.getMessage());
        }
    }

    @Override
    public List<Player> findAll() {
        return new ArrayList<Player>(this.cachedPlayers.values());
    }

    @Override
    public void save(Player player) {
        this.cachedPlayers.put(player.getId(), player);
        this.flushAsync(new ArrayList<Player>(this.cachedPlayers.values()));
    }

    @Override
    public void delete(Player player) {
        this.cachedPlayers.remove(player.getId());
        this.flushAsync(new ArrayList<Player>(this.cachedPlayers.values()));
    }

    @Override
    public void close() {
        log.info("Avvio spegnimento del layer di persistenza...");
        this.ioExecutor.shutdown();
        try {
            if (!this.ioExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                log.warn("Spegnimento forzato dei task I/O pendenti!");
                this.ioExecutor.shutdownNow();
            } else {
                log.info("Tutti i salvataggi sono stati completati con successo prima dell'uscita.");
            }
        } catch (InterruptedException e) {
            this.ioExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void flushAsync(List<Player> snapshot) {
        this.ioExecutor.submit(() -> {
            File tempFile = new File(this.saveFile.getAbsolutePath() + ".tmp");
            try {
                this.saveFile.getParentFile().mkdirs();
                this.mapper.writerWithDefaultPrettyPrinter().writeValue(tempFile, snapshot);
                Files.move(tempFile.toPath(), this.saveFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
                log.info("[I/O Queue] DB JSON aggiornato in sicurezza.");
            } catch (IOException e) {
                log.error("[I/O Queue] Errore critico I/O: {}", e.getMessage());
                if (tempFile.exists()) tempFile.delete();
            }
        });
    }
}