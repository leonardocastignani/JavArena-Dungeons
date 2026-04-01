package it.unicam.cs.mpgc.rpg125667.repository;

import it.unicam.cs.mpgc.rpg125667.model.*;
import jakarta.persistence.*;
import java.util.*;

public class PlayerRepository {
    
    private final EntityManagerFactory emf;

    public PlayerRepository() {
        this.emf = Persistence.createEntityManagerFactory("RpgPU");
    }

    public void save(Player player) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        
        if (player.getId() == null) {
            em.persist(player);
        } else {
            em.merge(player);
        }
        
        em.getTransaction().commit();
        em.close();
    }

    @SuppressWarnings("null")
    public Optional<Player> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        Player player = em.find(Player.class, id);
        em.close();
        return Optional.ofNullable(player);
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}