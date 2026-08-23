package it.unicam.cs.mpgc.rpg125667.repository;

import it.unicam.cs.mpgc.rpg125667.model.*;

import jakarta.persistence.*;

import lombok.extern.slf4j.*;

import java.util.*;

/**
 * Implementazione del repository basata su database H2 embedded, tramite JPA/Hibernate.
 * <p>
 * A differenza della precedente implementazione su file JSON, questa classe non mantiene
 * alcuna cache manuale in memoria né delega le scritture a un thread pool asincrono: ogni
 * operazione apre un {@link EntityManager} di breve durata, esegue una singola transazione
 * e la chiude. Su un database embedded locale il costo di questa scelta è trascurabile
 * (upsert/rimozione di una singola riga), mentre il beneficio è concreto: non esiste più
 * uno stato duplicato (cache applicativa vs file su disco) da tenere sincronizzato a mano,
 * e la coerenza tra dato persistito e oggetto {@link Player} è garantita direttamente dalle
 * transazioni JPA.
 * </p>
 */
@Slf4j
public class HibernatePlayerRepository implements IPlayerRepository {

    private final EntityManagerFactory emf;

    /**
     * Costruttore base. Inizializza la persistence unit {@code RpgPU} definita in
     * {@code META-INF/persistence.xml}, creando il file del database H2 se non esiste già.
     */
    public HibernatePlayerRepository() {
        this.emf = Persistence.createEntityManagerFactory("RpgPU");
    }

    /**
     * Recupera tutti i giocatori attualmente memorizzati nel database.
     *
     * @return Una lista contenente tutti i giocatori.
     */
    @Override
    public List<Player> findAll() {
        try (EntityManager em = this.emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Player p", Player.class).getResultList();
        }
    }

    /**
     * Salva o aggiorna un giocatore nel database.
     * <p>
     * Utilizza {@link EntityManager#merge(Object)}, che ha semantica di upsert corretta
     * poiché l'identificativo del giocatore è già assegnato al momento della creazione
     * (vedi {@link Player#Player(String, CharacterStats)}).
     * </p>
     *
     * @param player Il giocatore da salvare.
     */
    @Override
    public void save(Player player) {
        EntityManager em = this.emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(player);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            log.error("Errore durante il salvataggio del giocatore {}: {}", player.getId(), e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Rimuove un giocatore dal database.
     * <p>
     * Se il giocatore indicato non è presente, l'operazione non produce alcun effetto.
     * </p>
     *
     * @param player Il giocatore da rimuovere.
     */
    @Override
    public void delete(Player player) {
        EntityManager em = this.emf.createEntityManager();
        try {
            em.getTransaction().begin();
            @SuppressWarnings("null")
            Player managed = em.find(Player.class, player.getId());
            if (managed != null) em.remove(managed);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            log.error("Errore durante l'eliminazione del giocatore {}: {}", player.getId(), e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Chiude la {@link EntityManagerFactory}, rilasciando la connessione al database.
     */
    @Override
    public void close() {
        log.info("Avvio spegnimento del layer di persistenza...");
        this.emf.close();
        log.info("Connessione al database chiusa correttamente.");
    }
}