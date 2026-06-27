package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;
import it.unicam.cs.mpgc.rpg125667.engine.action.*;

import lombok.extern.slf4j.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * Controller JavaFX responsabile della gestione della schermata di combattimento (Arena).
 * Intercetta gli input dell'utente (Attacco, Cura, Fuga) e orchestra gli aggiornamenti
 * della UI in base ai risultati elaborati dal {@link it.unicam.cs.mpgc.rpg125667.engine.BattleEngine}.
 */
@Slf4j
public class ArenaController implements InjectableController {

    @FXML private Label playerNameLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label playerStatsLabel;
    @FXML private Label monsterNameLabel;
    @FXML private Label monsterHpLabel;
    @FXML private Label monsterStatsLabel;
    @FXML private TextArea battleLog;
    @FXML private Button attackButton;
    @FXML private Button healButton;
    @FXML private Button backButton;
    @FXML private Button saveButton;

    private BattleEngine engine;
    private GameService service;

    /**
     * Inizializza l'arena di combattimento, generando un nemico casuale calibrato sul livello 
     * del giocatore e istanziando il motore di battaglia.
     *
     * @param player Il giocatore corrente che sta affrontando la battaglia.
     */
    public void initData(Player player) {
        this.attackButton.setDisable(false);
        this.healButton.setDisable(false);
        this.backButton.setDisable(true);
        this.saveButton.setDisable(true);

        Monster randomEnemy = MonsterFactory.generateRandomMonster(player.getLevel());
        this.engine = new BattleEngine(player, randomEnemy);
        
        this.battleLog.clear();
        log.info("Inizializzazione battaglia: {} vs {}", player.getName(), randomEnemy.getName());
        System.out.println("\n========================================");
        this.logMessage("Un " + randomEnemy.getName() + " ti sbarra la strada!");
        this.logMessage("--- INIZIO BATTAGLIA ---");
        this.updateUI();
    }

    /**
     * Imposta il servizio di gioco associato al controller.
     *
     * @param service Il servizio di gioco da utilizzare.
     */
    @Override
    public void setGameService(GameService service) {
        this.service = service;
    }

    /**
     * Gestisce l'evento di click sul pulsante "Attacca".
     * Esegue il turno del giocatore e, se il nemico sopravvive, esegue il contrattacco.
     * Verifica inoltre la condizione di fine battaglia ad ogni scambio di colpi.
     */
    @FXML
    protected void onAttackClick() {
        TurnResult playerTurn = this.engine.executeAction(this.engine.getPlayer(),
                                                          this.engine.getMonster(),
                                                          new BasicAttackAction());
        this.logMessage(playerTurn.logMessage());

        if (this.engine.isBattleOver()) {
            this.endBattle();
            return;
        }

        TurnResult monsterTurn = this.engine.executeAction(this.engine.getMonster(),
                                                           this.engine.getPlayer(),
                                                           new BasicAttackAction());
        this.logMessage(monsterTurn.logMessage());

        this.updateUI();

        if (this.engine.isBattleOver()) this.endBattle();
    }

    /**
     * Gestisce l'evento di click sul pulsante "Usa Pozione".
     * Consuma una pozione e fa saltare l'attacco al giocatore, subendo direttamente 
     * il colpo del mostro nemico.
     */
    @FXML
    protected void onHealClick() {
        TurnResult healTurn = this.engine.executeAction(this.engine.getPlayer(),
                                                        this.engine.getMonster(),
                                                        new HealAction());
        this.logMessage(healTurn.logMessage());

        if (!this.engine.isBattleOver()) {
            TurnResult monsterTurn = this.engine.executeAction(this.engine.getMonster(),
                                                               this.engine.getPlayer(),
                                                               new BasicAttackAction());
            this.logMessage(monsterTurn.logMessage());
        }

        this.updateUI();

        if (this.engine.isBattleOver()) this.endBattle();
    }

    /**
     * Aggiorna la UI della schermata di combattimento, riflettendo lo stato
     * attuale del giocatore e del nemico.
     * Aggiorna le etichette dei nomi, HP, statistiche e il numero di pozioni disponibili.
     * Disabilita i pulsanti se la battaglia è terminata o se il giocatore non ha più pozioni.
     */
    private void updateUI() {
        this.playerNameLabel.setText(this.engine.getPlayer().getName()+ " (Lv. " + this.engine.getPlayer().getLevel() + ")");
        this.playerHpLabel.setText("HP: " + this.engine.getPlayer().getCurrentHealth());
        this.playerStatsLabel.setText("Att: " + this.engine.getPlayer().getStats().getBaseAttack() + 
                                 "  |  Dif: " + this.engine.getPlayer().getStats().getBaseDefense());
        
        this.monsterNameLabel.setText(this.engine.getMonster().getName());
        this.monsterHpLabel.setText("HP: " + this.engine.getMonster().getCurrentHealth());
        this.monsterStatsLabel.setText("Att: " + this.engine.getMonster().getStats().getBaseAttack() + 
                                  "  |  Dif: " + this.engine.getMonster().getStats().getBaseDefense());

        this.healButton.setText("Curati (" + this.engine.getPlayer().getPotions() + ")");
        boolean isBattleFinished = this.engine != null && this.engine.isBattleOver();
        this.healButton.setDisable(this.engine.getPlayer().getPotions() <= 0 || isBattleFinished);
    }

    /**
     * Aggiunge un nuovo messaggio testuale al log della battaglia visibile a schermo,
     * assicurandosi che il testo scorra automaticamente verso il basso.
     *
     * @param message Il testo descrittivo dell'azione appena avvenuta.
     */
    private void logMessage(String message) {
        if (message == null || message.isEmpty()) return;
        this.battleLog.appendText(message + "\n");
        log.debug("Azione: {}", message);
    }

    /**
     * Gestisce la logica di fine battaglia, aggiornando la UI e i pulsanti in base al risultato.
     * Se il giocatore vince, vengono concessi i premi e salvati i progressi; se perde,
     * viene reindirizzato alla schermata di Game Over. 
     */
    private void endBattle() {
        this.updateUI();
        this.logMessage("--- FINE BATTAGLIA ---");
        this.logMessage(this.engine.getBattleResult());
        
        this.attackButton.setDisable(true);
        this.healButton.setDisable(true);

        if (this.engine.getPlayer().isAlive()) {
            this.backButton.setDisable(false);
            this.saveButton.setDisable(false);

            String rewardLog = this.engine.grantRewards();
            this.logMessage(rewardLog);

            this.updateUI();

            this.monsterNameLabel.setText("VITTORIA!");
            this.monsterNameLabel.getStyleClass().add("victory-label");
            this.monsterHpLabel.setText("Nemico annientato");
            this.monsterStatsLabel.setText("💀"); 
            
            this.backButton.setText("Torna al Menu");
            if (!this.backButton.getStyleClass().contains("victory-button")) {
                this.backButton.getStyleClass().add("victory-button");
            }

            this.logMessage("La battaglia e' terminata. Se sei soddisfatto dell'esito, usa il pulsante 'Salva Partita' per mantenere i progressi!\nSalute rimanente: " + this.engine.getPlayer().getCurrentHealth() + " HP.");
        } else {
            this.logMessage("Sei morto... I tuoi progressi non verranno salvati.");
            this.service.deleteProgress(this.engine.getPlayer());
            this.goToGameOver();
        }
    }

    /**
     * Gestisce l'evento di click sul pulsante "Torna al Menu".
     * Chiude la schermata di combattimento e ritorna al menu principale del gioco.
     */
    @FXML
    protected void onBackToMenuClick() {
        Stage stage = (Stage) this.backButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml", this.service);
    }

    /**
     * Gestisce la transizione alla schermata di Game Over quando il giocatore perde la battaglia.
     * Chiude la schermata di combattimento e carica la scena di Game Over.
     */
    @FXML
    private void goToGameOver() {
        Stage stage = (Stage) this.attackButton.getScene().getWindow();
        SceneManager.switchScene(stage, "/it/unicam/cs/mpgc/rpg125667/view/game-over.fxml", this.service);
    }

    /**
     * Gestisce il salvataggio manuale richiesto dall'utente.
     */
    @FXML
    protected void onManualSaveClick() {
        if (this.engine.getPlayer().isAlive()) {
            this.engine.getPlayer().updateSaveDate();
            this.service.saveProgress(this.engine.getPlayer());
            this.logMessage("Partita salvata manualmente con successo!");
            this.saveButton.setDisable(true);
            this.saveButton.setText("Salvato!");
        } else {
            this.logMessage("Non puoi salvare da morto!");
        }
    }
}