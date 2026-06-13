package it.unicam.cs.mpgc.rpg125667.service;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;

import java.util.*;

/**
 * Rappresenta il Service Layer (Livello di Servizio) dell'applicazione.
 * Funge da orchestratore centrale, disaccoppiando completamente l'interfaccia 
 * utente (JavaFX Controllers) dal livello di persistenza dei dati (Repository).
 * * Centralizza la logica applicativa per garantire che le viste siano "stupide" 
 * e si limitino a delegare le azioni dell'utente.
 */
public class GameService {
    
    private final IPlayerRepository repository;

    /**
     * Costruisce il servizio iniettando la dipendenza del repository.
     *
     * @param repo Il repository da utilizzare per la persistenza dei giocatori.
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
     *
     * @param p Il giocatore da salvare.
     */
    public void saveProgress(Player player) {
        this.repository.save(player);
    }

    /**
     * Elimina definitivamente i dati di un giocatore.
     *
     * @param p Il giocatore da eliminare.
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