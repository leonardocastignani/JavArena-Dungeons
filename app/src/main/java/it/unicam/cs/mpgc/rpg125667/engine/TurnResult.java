package it.unicam.cs.mpgc.rpg125667.engine;

public record TurnResult(
    String logMessage,
    int damageDealt,
    boolean isCritical,
    boolean isDodge
) {}