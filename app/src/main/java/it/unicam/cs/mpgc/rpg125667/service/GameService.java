package it.unicam.cs.mpgc.rpg125667.service;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.repository.*;

import java.util.*;

public class GameService {
    
    private final IPlayerRepository repository;

    public GameService(IPlayerRepository repository) {
        this.repository = repository;
    }

    public List<Player> getAllSavedPlayers() {
        return this.repository.findAll();
    }

    public void saveProgress(Player player) {
        this.repository.save(player);
    }

    public void deleteProgress(Player player) {
        this.repository.delete(player);
    }

    public void shutdown() {
        this.repository.close();
    }
}