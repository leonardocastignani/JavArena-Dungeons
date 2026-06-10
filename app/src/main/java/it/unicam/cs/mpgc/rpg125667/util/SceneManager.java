package it.unicam.cs.mpgc.rpg125667.util;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;

public class SceneManager {

    public static void switchScene(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 600, 400));
        } catch (IOException e) {
            System.err.println("Errore fatale: Impossibile caricare la scena " + fxmlPath);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static <T> T switchSceneWithController(Stage stage, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 600, 400));
            return loader.getController();
        } catch (IOException e) {
            System.err.println("Errore fatale: Impossibile caricare la scena " + fxmlPath);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}