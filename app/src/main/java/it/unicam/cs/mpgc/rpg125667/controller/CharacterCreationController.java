package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

import java.util.*;

/**
 * Controller JavaFX responsabile della creazione di un nuovo personaggio.
 * Gestisce il rolling delle statistiche (dadi) e la validazione dei dati inseriti dall'utente.
 */
public class CharacterCreationController implements InjectableController {

    @FXML private TextField nameField;
    @FXML private Label errorLabel;
    @FXML private Label attackLabel;
    @FXML private Label defenseLabel;

    private GameService service;
    private Random random;

    private int currentAttack;
    private int currentDefense;

    /**
     * Inizializza il generatore di numeri casuali e fa un primo tiro per le statistiche.
     * Viene chiamato automaticamente da JavaFX al caricamento della vista.
     */
    @FXML
    public void initialize() {
        this.random = new Random();
        this.rollStats();
    }

    /**
     * Inietta il servizio di gioco nel controller.
     *
     * @param service Il servizio applicativo per l'accesso ai dati.
     */
    @Override
    public void setGameService(GameService service) {
        this.service = service;
    }

    /**
     * Gestisce il click sul pulsante "Ritira Dadi" per ricalcolare le statistiche casuali.
     */
    @FXML
    protected void onRollStatsClick() {
        this.rollStats();
    }

    /**
     * Genera valori casuali per Attacco e Difesa e aggiorna l'interfaccia grafica.
     */
    private void rollStats() {
        this.currentAttack = this.random.nextInt(11) + 10; 
        this.currentDefense = this.random.nextInt(7) + 2;

        this.attackLabel.setText(String.valueOf(this.currentAttack));
        this.defenseLabel.setText(String.valueOf(this.currentDefense));
    }

    /**
     * Gestisce il click sul pulsante di creazione dell'eroe.
     * Valida il nome inserito, istanzia il nuovo oggetto Player, lo salva e avvia l'Arena.
     */
    @FXML
    protected void onCreateHeroClick() {
        String heroName = this.nameField.getText().trim();

        if (heroName == null || heroName.trim().isEmpty()) {
            this.errorLabel.setText("Errore: Il nome non può essere vuoto.");
            return;
        }

        heroName = heroName.trim();

        if (heroName.length() < 3 || heroName.length() > 15) {
            this.errorLabel.setText("Errore: Il nome deve avere tra 3 e 15 caratteri.");
            return;
        }
        if (!heroName.matches("^[a-zA-Z0-9 ]+$")) {
            this.errorLabel.setText("Errore: Ammessi solo caratteri alfanumerici.");
            return;
        }

        CharacterStats stats = new CharacterStats(100, 100, this.currentAttack, this.currentDefense);
        Player newHero = new Player(heroName, stats);

        this.service.saveProgress(newHero);

        this.goToArena(newHero);
    }

    /**
     * Gestisce il ritorno al menu principale senza salvare alcun eroe.
     */
    @FXML
    protected void onBackToMenuClick() {
        Stage stage = (Stage) this.nameField.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.service);
    }

    /**
     * Cambia la scena attiva trasferendo il nuovo giocatore all'Arena.
     *
     * @param player Il personaggio appena creato.
     */
    private void goToArena(Player player) {
        Stage stage = (Stage) this.nameField.getScene().getWindow();
        ArenaController arenaController = SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/arena.fxml", this.service);
        arenaController.initData(player);
    }
}