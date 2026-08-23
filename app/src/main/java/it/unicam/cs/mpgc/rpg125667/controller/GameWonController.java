package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * Controller per la schermata di vittoria di partita.
 * <p>
 * Mostrato una tantum quando il giocatore raggiunge {@link it.unicam.cs.mpgc.rpg125667.util.GameConfig#VICTORY_LEVEL}
 * per la prima volta (vedi {@link it.unicam.cs.mpgc.rpg125667.model.Player#shouldShowVictoryScreen()}),
 * distinta dalla vittoria di una singola battaglia. L'eroe resta pienamente giocabile dopo questa schermata.
 * </p>
 */
public class GameWonController implements InjectableController {

    @FXML private Button menuButton;

    private GameService service;

    /**
     * Inietta il servizio di gioco all'interno del controller per permettere
     * un corretto rientro al menu principale mantenendo il collegamento al database.
     *
     * @param service L'istanza del servizio applicativo corrente.
     */
    @Override
    public void setGameService(GameService service) {
        this.service = service;
    }

    /**
     * Gestisce il click dell'utente sul pulsante "Ritorna al Menu", cambiando
     * la scena corrente e ricaricando l'hub principale del gioco.
     */
    @FXML
    protected void onBackToMenuClick() {
        Stage stage = (Stage) this.menuButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.service);
    }
}