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
    private final List<Player> cachedPlayers = new CopyOnWriteArrayList<Player>();    
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    public JsonPlayerRepository() {
        this.loadCache();
    }

    private void loadCache() {
        if (!this.saveFile.exists()) return;
        try {
            List<Player> loaded = this.mapper.readValue(this.saveFile, new TypeReference<List<Player>>() {});
            this.cachedPlayers.addAll(loaded);
        } catch (Exception e) {
            log.error("Errore lettura DB: {}", e.getMessage());
        }
    }

    @Override
    public List<Player> findAll() {
        return new ArrayList<Player>(this.cachedPlayers);
    }

    @Override
    public void save(Player player) {
        this.cachedPlayers.removeIf(p -> p.getId() != null && p.getId().equals(player.getId()));
        this.cachedPlayers.add(player);
        this.flushAsync(new ArrayList<Player>(this.cachedPlayers));
    }

    @Override
    public void delete(Player player) {
        this.cachedPlayers.removeIf(p -> p.getId() != null && p.getId().equals(player.getId()));
        this.flushAsync(new ArrayList<Player>(this.cachedPlayers));
    }

    @Override
    public void close() {
        this.ioExecutor.shutdown();
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
            } catch (IOException e) {
                log.error("[I/O Queue] Errore critico I/O: {}", e.getMessage());
                if (tempFile.exists()) tempFile.delete();
            }
        });
    }
}