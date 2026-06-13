package it.unicam.cs.mpgc.rpg125667.util;

import it.unicam.cs.mpgc.rpg125667.repository.*;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import lombok.extern.slf4j.*;

import java.io.*;

@Slf4j
public class SceneManager {

    public static <T> T switchScene(Stage stage, String fxmlPath, IPlayerRepository repository) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);

            String css = SceneManager.class.getResource("/it/unicam/cs/mpgc/rpg125667/style/style.css").toExternalForm();
            if (css != null) {
                scene.getStylesheets().add(css);
            }

            T controller = loader.getController();
            if (controller instanceof InjectableController injectable) {
                injectable.setRepository(repository);
            }

            stage.setScene(scene);
            stage.show();
            return controller;
            
        } catch (IOException e) {
            log.error("Errore fatale: Impossibile caricare la scena {}", fxmlPath, e);
            throw new RuntimeException("Impossibile caricare " + fxmlPath, e);
        }
    }
}