package it.unicam.cs.mpgc.rpg125667.engine;

/**
 * Data Transfer Object (DTO) immutabile che rappresenta l'esito di una singola azione di combattimento.
 * Sostituisce l'uso fragile delle stringhe per permettere alla UI di interpretare
 * e animare gli eventi in base a flag booleani (es. colpo critico o schivata).
 *
 * @param logMessage Il messaggio testuale dell'azione da mostrare a schermo.
 * @param damageDealt L'ammontare effettivo di danni inflitti dopo la riduzione difensiva.
 * @param isCritical True se l'attacco ha superato la soglia di critico.
 * @param isDodge True se il difensore ha schivato il colpo, annullando i danni.
 */
public record TurnResult(
    String logMessage,
    int damageDealt,
    boolean isCritical,
    boolean isDodge
) {}