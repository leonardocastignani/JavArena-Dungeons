package it.unicam.cs.mpgc.rpg125667.util;

import it.unicam.cs.mpgc.rpg125667.service.*;

/**
 * Interfaccia contrattuale per i Controller JavaFX che necessitano di dipendenze esterne.
 * <p>
 * Implementando questa interfaccia, i controller segnalano al {@link SceneManager} 
 * di essere pronti a ricevere l'iniezione del {@link GameService} tramite 
 * {@code setGameService}. Questo permette di mantenere il disaccoppiamento tra 
 * la vista (FXML) e il layer di logica applicativa.
 * </p>
 */
public interface InjectableController {
    
    /**
     * Inietta l'istanza del servizio di gioco all'interno del controller.
     * <p>
     * Questo metodo viene invocato automaticamente dal {@code ControllerFactory} 
     * durante il processo di caricamento delle scene tramite il {@link SceneManager}.
     * </p>
     *
     * @param service Il servizio di orchestrazione ({@link GameService}) istanziato 
     * dall'applicazione.
     */
    void setGameService(GameService service);
}