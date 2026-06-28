package it.unicam.cs.mpgc.rpg125667.controller;

import it.unicam.cs.mpgc.rpg125667.engine.*;
import it.unicam.cs.mpgc.rpg125667.model.*;
import it.unicam.cs.mpgc.rpg125667.service.*;
import it.unicam.cs.mpgc.rpg125667.util.*;
import it.unicam.cs.mpgc.rpg125667.engine.action.*;
import it.unicam.cs.mpgc.rpg125667.engine.generator.MonsterGenerator;
import it.unicam.cs.mpgc.rpg125667.engine.generator.RandomMonsterGenerator;
import lombok.extern.slf4j.*;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;

/**
 * Controller JavaFX responsabile della gestione della schermata di combattimento (Arena).
 * <p>
 * Questo controller coordina l'interazione tra l'interfaccia grafica e il {@link BattleEngine}.
 * Gestisce l'intero ciclo di vita di una battaglia, inclusi input utente (attacchi, pozioni),
 * aggiornamento del log di gioco, aggiornamento della UI e transizioni post-battaglia (vittoria/sconfitta).
 * </p>
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
    private int turnCounter = 1;

    /**
     * Inizializza l'arena di combattimento.
     * <p>
     * Effettua il setup dell'engine di gioco, genera un nemico casuale basato sul livello
     * del giocatore e configura lo stato iniziale della UI.
     * </p>
     *
     * @param player Il giocatore corrente che sta affrontando la battaglia.
     */
    public void initData(Player player) {
        this.attackButton.setDisable(false);
        this.healButton.setDisable(false);
        this.backButton.setDisable(true);
        this.saveButton.setDisable(true);

        MonsterGenerator generator = new RandomMonsterGenerator(MonsterLoader.getTemplates());
        Monster randomEnemy = generator.generate(player.getLevel());
        
        this.engine = new BattleEngine(player, randomEnemy);
        
        if (this.battleLog != null) {
            this.battleLog.clear();
        }
        this.turnCounter = 1;
        log.info("Inizializzazione battaglia: {} vs {}", player.getName(), randomEnemy.getName());
        System.out.println("\n========================================");
        this.logMessage("Un " + randomEnemy.getName() + " ti sbarra la strada!");
        this.logMessage("--- INIZIO BATTAGLIA ---");
        this.battleLog.appendText("\n");
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
     * <p>
     * Esegue il turno del giocatore invocando {@link BattleEngine#executeAction}. 
     * Se il mostro è ancora vivo, esegue automaticamente il contrattacco.
     * Al termine, verifica le condizioni di vittoria/sconfitta e aggiorna la UI.
     * </p>
     */
    @FXML
    protected void onAttackClick() {
        this.logMessage("--- TURNO " + this.turnCounter + " ---");

        TurnResult playerTurn = this.engine.executeAction(this.engine.getPlayer(),
                                                          this.engine.getMonster(),
                                                          new BasicAttackAction());
        this.logMessage(">> " + playerTurn.logMessage());

        if (this.engine.isBattleOver()) {
            this.endBattle();
            return;
        }

        TurnResult monsterTurn = this.engine.executeAction(this.engine.getMonster(),
                                                           this.engine.getPlayer(),
                                                           new BasicAttackAction());
        this.logMessage(">> " + monsterTurn.logMessage());

        this.battleLog.appendText("\n");
        this.turnCounter++;
        this.updateUI();

        if (this.engine.isBattleOver()) this.endBattle();
    }

    /**
     * Gestisce l'evento di click sul pulsante "Usa Pozione".
     * <p>
     * Consuma una pozione del giocatore. A differenza dell'attacco, il giocatore 
     * perde il suo turno di attacco, subendo il colpo del mostro (se ancora vivo).
     * </p>
     */
    @FXML
    protected void onHealClick() {
        this.logMessage("--- TURNO " + this.turnCounter + " ---");

        TurnResult healTurn = this.engine.executeAction(this.engine.getPlayer(),
                                                        this.engine.getMonster(),
                                                        new HealAction());
        this.logMessage(">> " + healTurn.logMessage());

        if (!this.engine.isBattleOver()) {
            TurnResult monsterTurn = this.engine.executeAction(this.engine.getMonster(),
                                                               this.engine.getPlayer(),
                                                               new BasicAttackAction());
            this.logMessage(">> " + monsterTurn.logMessage());
        }

        this.battleLog.appendText("\n"); // Riga vuota
        this.turnCounter++;
        this.updateUI();

        if (this.engine.isBattleOver()) this.endBattle();
    }

    /**
     * Aggiorna la UI della schermata di combattimento sincronizzandola con lo stato attuale 
     * del {@link BattleEngine}.
     * <p>
     * Aggiorna etichette (HP, statistiche, nomi) e gestisce lo stato di abilitazione
     * dei bottoni (es. disabilita i pulsanti se le pozioni sono esaurite).
     * </p>
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
     * Gestisce la logica di fine battaglia.
     * <p>
     * In caso di vittoria: premia il giocatore, abilita il salvataggio e permette di tornare al menu.
     * In caso di sconfitta: elimina il progresso salvato e reindirizza alla schermata di Game Over.
     * </p>
     */
    private void endBattle() {
        this.updateUI();
        this.logMessage("--- FINE BATTAGLIA ---");
        this.battleLog.appendText("\n");
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