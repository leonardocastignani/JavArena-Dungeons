package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;

import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.util.*;


/**
 * Controller JavaFX per la schermata di caricamento dei salvataggi.
 * <p>
 * Permette all'utente di selezionare un personaggio esistente da una {@link ListView}.
 * Visualizza dinamicamente i dettagli statistici dell'eroe selezionato.
 * </p>
 */
public class LoadGameController implements InjectableController {

    @FXML private ListView<Player> playerListView;
    @FXML private Label errorLabel;
    @FXML private VBox detailsPanel;
    @FXML private Label detailNameLabel;
    @FXML private Label detailLevelLabel;
    @FXML private Label detailHpLabel;
    @FXML private Label detailXpLabel;
    @FXML private Label detailStatsLabel;
    @FXML private Label detailPotionsLabel;
    @FXML private Label detailSaveDateLabel;
    
    private GameService service;

    /**
     * Inizializza i componenti grafici.
     * <p>
     * Configura la {@code ListCell} per mostrare il formato personalizzato del giocatore
     * e aggiunge un listener alla selezione per aggiornare il pannello dei dettagli.
     * Se il {@link GameService} è già stato iniettato al momento della chiamata,
     * avvia immediatamente il caricamento della lista dei salvataggi tramite
     * {@link #loadPlayers()}.
     * </p>
     */
    @FXML
    public void initialize() {
        this.playerListView.setCellFactory(param -> new ListCell<Player>() {
            @Override
            protected void updateItem(Player player, boolean empty) {
                super.updateItem(player, empty);
                if (empty || player == null) {
                    setText(null);
                } else {
                    String date = (player.getLastSaveDate() != null) ? player.getLastSaveDate() : "Vecchia Partita";
                    setText(player.getName() + " (Liv. " + player.getLevel() + ") - " + date);
                }
            }
        });
        
        this.playerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) this.showPlayerDetails(newValue);
        });

        if (this.service != null) {
            this.loadPlayers();
        }
    }

    /**
     * Inietta il servizio di gioco e avvia il popolamento della lista dei salvataggi.
     *
     * @param service Il servizio applicativo (GameService) per l'accesso ai dati.
     */
    @Override
    public void setGameService(GameService service) {
        this.service = service;
    }

    /**
     * Recupera la lista dei giocatori salvati dal database e la inserisce nella ListView.
     */
    private void loadPlayers() {
        if (this.service == null) return;
        
        List<Player> players = this.service.getAllSavedPlayers();
        ObservableList<Player> observablePlayers = FXCollections.observableArrayList(players);
        this.playerListView.setItems(observablePlayers);
        
        this.playerListView.getSelectionModel().clearSelection();
    }

    /**
     * Aggiorna il pannello laterale visibile con i dati del giocatore selezionato.
     *
     * @param p Il giocatore di cui mostrare le statistiche.
     */
    private void showPlayerDetails(Player p) {
        this.detailsPanel.setVisible(true);
        this.errorLabel.setText("");

        this.detailNameLabel.setText(p.getName());
        this.detailLevelLabel.setText("Livello: " + p.getLevel());
        this.detailHpLabel.setText("Salute: " + p.getCurrentHealth() + " / " + p.getStats().getMaxHealth());
        this.detailXpLabel.setText("XP: " + p.getXp() + " / " + (p.getLevel() * GameConfig.LEVEL_UP_XP_MULTIPLIER));
        this.detailStatsLabel.setText("Att: " + p.getStats().getBaseAttack() + "  |  Dif: " + p.getStats().getBaseDefense());
        this.detailPotionsLabel.setText("Pozioni rimanenti: " + p.getPotions());
        String dateToShow = (p.getLastSaveDate() != null) ? p.getLastSaveDate() : "Data sconosciuta";
        if (this.detailSaveDateLabel != null) {
            this.detailSaveDateLabel.setText("Ultimo Salvataggio: " + dateToShow);
        }
    }

    /**
     * Gestisce il click sul pulsante di caricamento dell'eroe.
     * Se un eroe è selezionato, avvia una nuova battaglia nell'Arena.
     */
    @FXML
    protected void onLoadHeroClick() {
        Player selectedPlayer = this.playerListView.getSelectionModel().getSelectedItem();

        if (selectedPlayer == null) {
            this.errorLabel.setText("Devi selezionare un eroe dalla lista!");
            return;
        }

        this.goToArena(selectedPlayer);
    }

    /**
     * Gestisce il click sul pulsante per tornare al menu principale.
     */
    @FXML
    protected void onBackToMenuClick() {
        Stage stage = (Stage) this.playerListView.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.service);
    }

    /**
     * Cambia la scena attiva portando il giocatore nell'Arena di combattimento.
     *
     * @param player Il giocatore che deve affrontare la battaglia.
     */
    private void goToArena(Player player) {
        Stage stage = (Stage) this.playerListView.getScene().getWindow();
        ArenaController arenaController = SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/arena.fxml", this.service);
        arenaController.initData(player);
    }

    /**
     * Forza l'aggiornamento della lista dei salvataggi e resetta la visibilità del pannello dettagli.
     * Utile quando la scena viene recuperata dalla cache di {@link SceneManager}.
     */
    public void refreshData() {
        this.loadPlayers();
        this.detailsPanel.setVisible(false);
    }
}