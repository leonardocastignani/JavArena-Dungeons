package it.unicam.cs.mpgc.rpg125667.engine.outcome;

/**
 * Porta (nel senso della Dependency Inversion) che disaccoppia la risoluzione degli esiti di
 * battaglia dalla schermata grafica che li presenta.
 * <p>
 * I {@link BattleOutcomeHandler} dipendono solo da questa interfaccia, non dal controller JavaFX
 * concreto: in questo modo la logica di gioco (chi vince, chi perde, cosa succede dopo) resta
 * nel package {@code engine}, libera da qualunque dipendenza da JavaFX.
 * </p>
 */
public interface BattleOutcomeView {

    /**
     * Aggiunge un messaggio testuale al log di battaglia visibile a schermo.
     *
     * @param message Il testo da mostrare.
     */
    void logMessage(String message);

    /**
     * Effettua la transizione verso la schermata di sconfitta (Game Over).
     */
    void transitionToDefeat();

    /**
     * Effettua la transizione verso la schermata di vittoria di partita.
     */
    void transitionToVictory();

    /**
     * Abilita la UI di prosecuzione della partita dopo una battaglia vinta non definitiva
     * (pulsanti "Prossima Battaglia" e "Salva Partita").
     *
     * @param remainingHealthMessage Messaggio riassuntivo della salute residua del giocatore.
     */
    void enableContinuation(String remainingHealthMessage);
}