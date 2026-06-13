package it.unicam.cs.mpgc.rpg125667.util;

import it.unicam.cs.mpgc.rpg125667.service.*;

/**
 * Interfaccia implementata da tutti i Controller JavaFX che necessitano di interagire
 * con il livello di servizio dell'applicazione. Permette la Dependency Injection nativa.
 */
public interface InjectableController {
    
    /**
     * Inietta l'istanza del servizio di gioco all'interno del controller.
     *
     * @param service Il servizio di orchestrazione (GameService) da utilizzare.
     */
    void setGameService(GameService service);
}