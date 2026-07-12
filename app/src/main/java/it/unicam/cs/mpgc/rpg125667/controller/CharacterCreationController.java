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
 * <p>
 * Gestisce la generazione casuale delle statistiche iniziali tramite rolling di dadi 
 * e la validazione rigorosa dei dati inseriti dall'utente (nome, unicità, formato).
 * </p>
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
     * Genera valori casuali per Attacco (compreso tra 10 e 20) e Difesa
     * (compreso tra 2 e 8) e aggiorna le rispettive label nella UI.
     */
    private void rollStats() {
        this.currentAttack = this.random.nextInt(11) + 10; 
        this.currentDefense = this.random.nextInt(7) + 2;

        this.attackLabel.setText(String.valueOf(this.currentAttack));
        this.defenseLabel.setText(String.valueOf(this.currentDefense));
    }

    /**
     * Gestisce il processo di creazione dell'eroe dopo il click sul pulsante di conferma.
     * <p>
     * Esegue i seguenti controlli di validazione:
     * <ol>
     * <li>Nome non vuoto.</li>
     * <li>Lunghezza compresa tra 3 e 15 caratteri.</li>
     * <li>Formato alfanumerico (spazi inclusi).</li>
     * <li>Unicità del nome rispetto ai salvataggi esistenti.</li>
     * </ol>
     * Se la validazione fallisce, aggiorna la {@code errorLabel}. Se ha successo,
     * salva il personaggio e avvia l'Arena.
     * </p>
     */
    @FXML
    protected void onCreateHeroClick() {
        final String heroName = this.nameField.getText().trim();

        if (heroName == null || heroName.isEmpty()) {
            this.errorLabel.setText("Errore: Il nome non può essere vuoto.");
            return;
        }

        if (heroName.length() < 3 || heroName.length() > 15) {
            this.errorLabel.setText("Errore: Il nome deve avere tra 3 e 15 caratteri.");
            return;
        }
        if (!heroName.matches("^[a-zA-Z0-9 ]+$")) {
            this.errorLabel.setText("Errore: Ammessi solo caratteri alfanumerici.");
            return;
        }

        boolean nameAlreadyExists = this.service.getAllSavedPlayers()
                                        .stream()
                                        .anyMatch(p -> p.getName().equalsIgnoreCase(heroName));

        if (nameAlreadyExists) {
            this.errorLabel.setText("Errore: Il nome è già stato utilizzato.");
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