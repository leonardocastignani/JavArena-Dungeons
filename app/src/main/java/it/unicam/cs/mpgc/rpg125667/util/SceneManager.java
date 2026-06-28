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
 */
@Slf4j
public class SceneManager {

    private static final Map<String, Parent> viewCache = new HashMap<String, Parent>();
    private static final Map<String, Object> controllerCache = new HashMap<String, Object>();

    /**
     * Carica o recupera dalla cache una scena FXML, inietta il service nel controller 
     * associato e aggiorna lo {@code Stage} corrente.
     *
     * @param stage    La finestra (Stage) principale dell'applicazione.
     * @param fxmlPath Il percorso assoluto della risorsa FXML da caricare (es. "/view/file.fxml").
     * @param service  Il servizio di gioco da iniettare nei controller.
     * @param <T>      Il tipo del controller associato.
     * 
     * @return L'istanza del controller caricato.
     * @throws RuntimeException se il file FXML non è trovabile o se si verifica un errore durante l'iniezione.
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