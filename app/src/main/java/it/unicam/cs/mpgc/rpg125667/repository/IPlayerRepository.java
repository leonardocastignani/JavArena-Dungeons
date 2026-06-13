package it.unicam.cs.mpgc.rpg125667.repository;

import it.unicam.cs.mpgc.rpg125667.model.*;

import java.util.*;

/**
 * Interfaccia che definisce le operazioni CRUD (Create, Read, Update, Delete)
 * per la persistenza delle entità {@link Player}.
 */
public interface IPlayerRepository {

    /**
     * Recupera tutti i giocatori memorizzati.
     *
     * @return Una lista di giocatori.
     */
    List<Player> findAll();

    /**
     * Salva o aggiorna un giocatore nel database.
     *
     * @param player L'oggetto Player da salvare.
     */
    void save(Player player);

    /**
     * Rimuove un giocatore dal database.
     *
     * @param player L'oggetto Player da rimuovere.
     */
    void delete(Player player);
    
    /**
     * Chiude le risorse in uso dal repository.
     */
    void close();
}