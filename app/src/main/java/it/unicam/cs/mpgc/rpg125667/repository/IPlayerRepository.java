package it.unicam.cs.mpgc.rpg125667.repository;

import it.unicam.cs.mpgc.rpg125667.model.*;

import java.util.*;

/**
 * Interfaccia che definisce le operazioni CRUD (Create, Read, Update, Delete)
 * per la persistenza delle entità {@link Player}.
 * <p>
 * L'interfaccia non impone alcuna garanzia specifica su thread-safety, sincronia
 * delle operazioni di I/O o supporto di storage sottostante: tali dettagli sono
 * demandati alle singole implementazioni (es. {@link JsonPlayerRepository}), che
 * devono documentarli esplicitamente.
 * </p>
 */
public interface IPlayerRepository {

    /**
     * Recupera tutti i giocatori attualmente memorizzati.
     * <p>
     * L'ordine degli elementi restituiti non è garantito e la lista ottenuta
     * è una copia indipendente dallo stato interno del repository: eventuali
     * modifiche ad essa non si ripercuotono sui dati persistiti.
     * </p>
     *
     * @return Una lista di giocatori; una lista vuota se non è presente alcun dato.
     */
    List<Player> findAll();

    /**
     * Salva o aggiorna un giocatore nel repository.
     * <p>
     * L'operazione ha semantica di "upsert": se esiste già un giocatore con lo
     * stesso identificativo ({@link Player#getId()}) i suoi dati vengono
     * sovrascritti, altrimenti viene creato un nuovo record.
     * </p>
     *
     * @param player L'oggetto Player da salvare.
     */
    void save(Player player);

    /**
     * Rimuove un giocatore dal repository.
     * <p>
     * Se il giocatore indicato non è presente, l'operazione non produce alcun
     * effetto (non viene sollevata alcuna eccezione).
     * </p>
     *
     * @param player L'oggetto Player da rimuovere.
     */
    void delete(Player player);

    /**
     * Chiude le risorse in uso dal repository (es. thread di I/O, handle su file),
     * garantendo il completamento delle eventuali operazioni di persistenza pendenti.
     * <p>
     * Deve essere invocato una sola volta, tipicamente allo spegnimento
     * dell'applicazione, per assicurare un rilascio ordinato delle risorse.
     * </p>
     */
    void close();
}