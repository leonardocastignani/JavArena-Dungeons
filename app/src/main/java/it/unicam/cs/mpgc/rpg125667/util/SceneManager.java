package it.unicam.cs.mpgc.rpg125667.util;

import it.unicam.cs.mpgc.rpg125667.service.*;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import lombok.extern.slf4j.*;

import java.util.*;

/**
 * Gestore centralizzato e ottimizzato per la navigazione tra le scene di JavaFX.
 * * Implementa due pattern architetturali avanzati:
 * 1. View Caching: Mantiene in memoria i {@link javafx.scene.Parent} già parsati 
 * per azzerare l'overhead di I/O e prevenire Memory Leak durante i cambi schermata.
 * 2. Dependency Injection: Utilizza il ControllerFactory nativo di {@link javafx.fxml.FXMLLoader} 
 * per iniettare dinamicamente i servizi nei controller a tempo di istanziazione.
 */
@Slf4j
public class SceneManager {

    private static final Map<String, Parent> viewCache = new HashMap<String, Parent>();
    private static final Map<String, Object> controllerCache = new HashMap<String, Object>();

    /**
     * Carica o recupera dalla cache una scena FXML, inietta il service nel controller 
     * e la mostra nello stage corrente.
     *
     * @param stage    La finestra principale dell'applicazione.
     * @param fxmlPath Il percorso assoluto della risorsa FXML da caricare.
     * @param service  Il GameService da iniettare nel controller della scena.
     * @param <T>      Il tipo inferito del Controller restituito.
     * @return L'istanza del Controller associato alla scena, utile per inizializzazioni post-caricamento.
     * @throws RuntimeException in caso di file FXML mancante o malformato.
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