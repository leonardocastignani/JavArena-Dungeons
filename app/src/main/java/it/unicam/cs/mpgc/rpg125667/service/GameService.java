package it.unicam.cs.mpgc.rpg125667.service;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;

import java.util.*;

/**
 * Rappresenta il Service Layer (Livello di Servizio) dell'applicazione.
 * <p>
 * Funge da orchestratore centrale, disaccoppiando completamente l'interfaccia 
 * utente (Controller JavaFX) dal livello di persistenza dei dati (Repository).
 * </p>
 * <p>
 * **Responsabilità:**
 * <ul>
 * <li>Centralizzare la logica applicativa.</li>
 * <li>Garantire che le viste siano "stupide", limitandosi a invocare metodi del servizio.</li>
 * <li>Gestire il ciclo di vita del layer di persistenza.</li>
 * </ul>
 * </p>
 */
public class GameService {
    
    private final IPlayerRepository repository;

    /**
     * Costruisce il servizio iniettando la dipendenza del repository.
     *
     * @param repository Il repository da utilizzare per la persistenza dei giocatori.
     */
    public GameService(IPlayerRepository repository) {
        this.repository = repository;
    }

    /**
     * Recupera la lista di tutti i giocatori salvati.
     *
     * @return Una lista contenente tutti i giocatori presenti nel database.
     */
    public List<Player> getAllSavedPlayers() {
        return this.repository.findAll();
    }

    /**
     * Salva o aggiorna i dati di un giocatore.
     * <p>
     * Il salvataggio è manuale: questo metodo non viene invocato automaticamente
     * a intervalli regolari, ma esclusivamente su richiesta esplicita dell'utente
     * (es. pressione del pulsante "Salva Partita" nell'interfaccia grafica).
     * </p>
     *
     * @param player Il giocatore da salvare.
     */
    public void saveProgress(Player player) {
        this.repository.save(player);
    }

    /**
     * Elimina definitivamente i dati di un giocatore.
     *
     * @param player Il giocatore da eliminare.
     */
    public void deleteProgress(Player player) {
        this.repository.delete(player);
    }

    /**
     * Termina in modo sicuro i processi di background e chiude la connessione al database.
     */
    public void shutdown() {
        this.repository.close();
    }
}