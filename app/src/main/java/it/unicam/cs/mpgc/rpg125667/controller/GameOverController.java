package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * Controller JavaFX responsabile della schermata di "Game Over".
 * Viene mostrato quando il giocatore esaurisce i punti vita nell'Arena.
 */
public class GameOverController implements InjectableController {

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
        Stage stage = (Stage) menuButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.service);
    }
}