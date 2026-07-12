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
 * <p>
 * **Architettura di persistenza:**
 * <ul>
 * <li><b>Cache:</b> Utilizza {@link ConcurrentHashMap} per accessi O(1) in memoria, garantendo la thread-safety.</li>
 * <li><b>I/O Asincrono:</b> Il metodo {@code flushAsync} delega la scrittura su disco a un {@code ExecutorService} dedicato, evitando blocchi del thread principale (UI).</li>
 * <li><b>Atomicità:</b> Le scritture avvengono su file temporanei seguite da {@code ATOMIC_MOVE}, garantendo che il file di salvataggio non venga mai corrotto durante il processo di I/O.</li>
 * </ul>
 * <p>
 * Il salvataggio non è automatico: {@link #save(Player)} viene invocato esplicitamente
 * dal livello superiore (tipicamente in risposta a un'azione manuale dell'utente, come
 * la pressione del pulsante "Salva Partita" nell'interfaccia), non da un timer o da un
 * meccanismo periodico interno a questa classe.
 * </p>
 */
@Slf4j
public class JsonPlayerRepository implements IPlayerRepository {

    private final File saveFile = new File("data/saves/savegame.json");
    private final ObjectMapper mapper = new ObjectMapper();
    
    private final Map<String, Player> cachedPlayers = new ConcurrentHashMap<String, Player>();
    
    private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor();

    /**
     * Costruttore base. Inizializza il repository caricando in modo sincrono
     * (bloccante) la cache in memoria a partire dal file di salvataggio su disco,
     * se presente.
     */
    public JsonPlayerRepository() {
        this.loadCache();
    }

    /**
     * Legge il file JSON (se esiste) e popola la cache in memoria all'avvio.
     * <p>
     * In caso di errore di lettura o parsing, l'eccezione viene intercettata e
     * loggata: il repository prosegue comunque l'inizializzazione con la cache
     * vuota, senza propagare l'errore al chiamante.
     * </p>
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
     * Esegue lo shutdown del layer di persistenza.
     * <p>
     * Tenta una chiusura "graceful" attendendo fino a 5 secondi il completamento dei salvataggi in coda.
     * In caso di mancato completamento, forza la terminazione.
     * </p>
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
     * <p>
     * La scrittura avviene prima su un file temporaneo ({@code .tmp}); solo al termine
     * della serializzazione il file temporaneo viene spostato sopra al file di
     * salvataggio definitivo tramite {@link StandardCopyOption#ATOMIC_MOVE}, in modo
     * che un eventuale crash o errore durante la scrittura non lasci mai il file di
     * salvataggio in uno stato parziale o corrotto. In caso di errore di I/O,
     * l'eccezione viene loggata e il file temporaneo residuo viene eliminato.
     * </p>
     *
     * @param snapshot La "fotografia" (copia immutabile al momento della chiamata)
     *                 della lista di giocatori da salvare.
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