package it.unicam.cs.mpgc.rpg125667.repository;

import it.unicam.cs.mpgc.rpg125667.model.*;

import java.util.*;

public interface IPlayerRepository {

    List<Player> findAll();

    void save(Player player);

    void delete(Player player);
    
    void close();
}