package it.unicam.cs.mpgc.rpg125667.util;

import it.unicam.cs.mpgc.rpg125667.repository.*;

public class ServiceLocator {
    private static IPlayerRepository playerRepository;

    public static IPlayerRepository getPlayerRepository() {
        if (playerRepository == null) {
            playerRepository = new JsonPlayerRepository();
        }
        return playerRepository;
    }
}