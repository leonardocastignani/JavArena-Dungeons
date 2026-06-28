package it.unicam.cs.mpgc.rpg125667.model;

/**
 * Interfaccia che definisce il contratto per tutte le entità partecipi a una battaglia.
 * <p>
 * Qualsiasi classe che implementa {@code Combatant} deve essere in grado di fornire
 * informazioni di stato (nome, salute) e gestire autonomamente la logica di ricezione danni
 * (mitigazione in base alle proprie statistiche).
 * </p>
 */
public interface Combatant {
    
    /**
     * Recupera il nome del combattente.
     *
     * @return Il nome come stringa.
     */
    String getName();
    
    /**
     * Recupera la salute corrente del combattente.
     *
     * @return Il valore della salute attuale.
     */
    int getCurrentHealth();
    
    /**
     * Verifica se il combattente ha ancora punti vita.
     *
     * @return true se la salute è maggiore di zero, false altrimenti.
     */
    boolean isAlive();
    
    /**
     * Applica un quantitativo di danni all'entità, delegando ad essa la logica
     * di riduzione tramite le proprie statistiche difensive.
     *
     * @param rawDamage Il danno grezzo calcolato prima della mitigazione.
     * @return Il danno effettivo che l'entità ha subito.
     */
    int takeDamage(int rawDamage);

    /**
     * Restituisce le statistiche base e attuali del combattente.
     *
     * @return L'oggetto {@link CharacterStats} contenente i parametri.
     */
    CharacterStats getStats();
}