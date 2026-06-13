package it.unicam.cs.mpgc.rpg125667.util;

import it.unicam.cs.mpgc.rpg125667.repository.*;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;

public class SceneManager {

    public static void switchScene(Stage stage, String fxmlPath, IPlayerRepository repository) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);

            String css = SceneManager.class.getResource("/it/unicam/cs/mpgc/rpg125667/style/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            Object controller = loader.getController();
            if (controller instanceof InjectableController) {
                ((InjectableController) controller).setRepository(repository);
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T switchSceneWithController(Stage stage, String fxmlPath, IPlayerRepository repository) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 400);

            String css = SceneManager.class.getResource("/it/unicam/cs/mpgc/rpg125667/style/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            Object controller = loader.getController();
            if (controller instanceof InjectableController) {
                ((InjectableController) controller).setRepository(repository);
            }

            stage.setScene(scene);
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}