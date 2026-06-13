package it.unicam.cs.mpgc.rpg125667.util;

import it.unicam.cs.mpgc.rpg125667.service.*;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import lombok.extern.slf4j.*;

import java.util.*;

@Slf4j
public class SceneManager {

    private static final Map<String, Parent> viewCache = new HashMap<String, Parent>();
    private static final Map<String, Object> controllerCache = new HashMap<String, Object>();

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
                scene = new Scene(root, 600, 400);
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