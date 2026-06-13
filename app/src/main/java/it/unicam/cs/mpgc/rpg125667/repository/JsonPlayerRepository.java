package it.unicam.cs.mpgc.rpg125667.repository;

import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;

import it.unicam.cs.mpgc.rpg125667.model.*;

import lombok.extern.slf4j.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Implementazione thread-safe del repository basata su file JSON.
 * * Ottimizzazioni implementate:
 * - Letture O(1) in memoria grazie all'uso di una {@link java.util.concurrent.ConcurrentHashMap}.
 * - Scritture Asincrone: L'I/O su disco è delegato a un SingleThreadExecutor per non bloccare la UI.
 * - Atomicità: Utilizza {@link java.nio.file.StandardCopyOption#ATOMIC_MOVE} per prevenire corruzioni.
 * - Graceful Shutdown: Implementa un'attesa attiva alla chiusura per svuotare la coda di salvataggio.
 */
@Slf4j
public class JsonPlayerRepository implements IPlayerRepository {

    private final File saveFile = new File("data/saves/savegame.json");
    private final ObjectMapper mapper = new ObjectMapper();
    
    private final Map<String, Player> cachedPlayers = new ConcurrentHashMap<String, Player>();
    
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore base. Inizializza il repository caricando la cache dal disco.
     */
    public JsonPlayerRepository() {
        this.loadCache();
    }

    /**
     * Legge il file JSON (se esiste) e popola la cache in memoria all'avvio.
     */
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

    /**
     * Restituisce tutti i giocatori attualmente nella cache.
     *
     * @return Una lista contenente tutti i giocatori.
     */
    @Override
    public List<Player> findAll() {
        return new ArrayList<Player>(this.cachedPlayers.values());
    }

    /**
     * Aggiunge o aggiorna un giocatore nella cache e richiede un salvataggio asincrono su disco.
     *
     * @param player Il giocatore da salvare.
     */
    @Override
    public void save(Player player) {
        this.cachedPlayers.put(player.getId(), player);
        this.flushAsync(new ArrayList<Player>(this.cachedPlayers.values()));
    }

    /**
     * Rimuove un giocatore dalla cache e richiede un salvataggio asincrono su disco.
     *
     * @param player Il giocatore da eliminare.
     */
    @Override
    public void delete(Player player) {
        this.cachedPlayers.remove(player.getId());
        this.flushAsync(new ArrayList<Player>(this.cachedPlayers.values()));
    }

    /**
     * Esegue lo spegnimento sicuro del thread pool di I/O.
     * Attende fino a 5 secondi il completamento dei salvataggi pendenti prima di forzare la chiusura,
     * garantendo l'integrità dei salvataggi in fase di uscita dall'applicazione.
     */
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

    /**
     * Invia un task al thread di I/O per serializzare la cache aggiornata su file.
     * Usa la scrittura atomica su file temporaneo per prevenire corruzioni.
     *
     * @param snapshot La "fotografia" della lista di giocatori da salvare.
     */
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