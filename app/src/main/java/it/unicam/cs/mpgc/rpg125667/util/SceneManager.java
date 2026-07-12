package it.unicam.cs.mpgc.rpg125667.util;

import it.unicam.cs.mpgc.rpg125667.service.*;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import lombok.extern.slf4j.*;

import java.util.*;

/**
 * Gestore centralizzato per la navigazione tra le scene di JavaFX.
 * <p>
 * Implementa due strategie ottimizzative per la gestione della UI:
 * <ul>
 * <li><b>View Caching:</b> Mantiene in memoria i nodi radice (Parent) già parsati 
 * per eliminare l'overhead di I/O (caricamento FXML) e prevenire Memory Leak 
 * durante i frequenti cambi di schermata.</li>
 * <li><b>Dependency Injection:</b> Utilizza un {@code ControllerFactory} personalizzato
 * per iniettare dinamicamente il {@link GameService} nei controller che
 * implementano {@link InjectableController}.</li>
 * </ul>
 * </p>
 * <p>
 * Lo stato delle cache è mantenuto in campi {@code static} non sincronizzati: la
 * classe è quindi pensata per essere utilizzata esclusivamente dal thread
 * dell'applicazione JavaFX (JavaFX Application Thread) e non è thread-safe.
 * </p>
 */
@Slf4j
public class SceneManager {

    private static final Map<String, Parent> viewCache = new HashMap<String, Parent>();
    private static final Map<String, Object> controllerCache = new HashMap<String, Object>();

    /**
     * Carica o recupera dalla cache una scena FXML, inietta il service nel controller
     * associato e aggiorna lo {@code Stage} corrente.
     * <p>
     * Se la scena richiesta è già presente in cache (chiamata successiva alla prima
     * per lo stesso {@code fxmlPath}), il file FXML non viene riletto né riparsato:
     * vengono riutilizzati il nodo radice e il controller già creati in precedenza.
     * Se lo {@code Stage} non ha ancora una {@link Scene} associata ne viene creata
     * una nuova (dimensione 800x600) con il foglio di stile dell'applicazione; in
     * caso contrario viene semplicemente sostituita la radice della scena esistente.
     * </p>
     *
     * @param stage    La finestra (Stage) principale dell'applicazione.
     * @param fxmlPath Il percorso assoluto della risorsa FXML da caricare (es. "/view/file.fxml").
     * @param service  Il servizio di gioco da iniettare nei controller che implementano
     *                 {@link InjectableController}.
     * @param <T>      Il tipo del controller associato.
     *
     * @return L'istanza del controller caricato (dalla cache o appena creato).
     * @throws RuntimeException se il file FXML non è trovabile o se si verifica un errore durante il caricamento o l'iniezione del servizio.
     */
    @SuppressWarnings("unchecked")
    public static <T> T switchScene(Stage stage, String fxmlPath, GameService service) {
        try {
            Parent root = viewCache.get(fxmlPath);
            T controller = (T) controllerCache.get(fxmlPath);

            if (root == null) {
                log.info("Cache Miss: Caricamento e parsing di {}", fxmlPath);
                FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
                loader.setControllerFactory(param -> {
                    try {
                        Object c = param.getDeclaredConstructor().newInstance();
                        if (c instanceof InjectableController injectable) injectable.setGameService(service);
                        return c;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                root = loader.load();
                controller = loader.getController();
                
                viewCache.put(fxmlPath, root);
                controllerCache.put(fxmlPath, controller);
            }

            Scene scene = stage.getScene();
            if (scene == null) {
                scene = new Scene(root, 800, 600);
                String css = SceneManager.class.getResource("/it/unicam/cs/mpgc/rpg125667/style/style.css").toExternalForm();
                if (css != null) scene.getStylesheets().add(css);
                stage.setScene(scene);
            } else {
                scene.setRoot(root);
            }

            stage.show();
            return controller;
        } catch (Exception e) {
            log.error("Errore fatale: Impossibile caricare la scena {}", fxmlPath, e);
            throw new RuntimeException(e);
        }
    }
}