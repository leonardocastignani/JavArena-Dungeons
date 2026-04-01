package it.unicam.cs.mpgc.rpg125667;

import it.unicam.cs.mpgc.rpg125667.controller.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import java.io.*;

public class App extends Application {

    private MainMenuController mainController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/it/unicam/cs/mpgc/rpg125667/view/main-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        mainController = fxmlLoader.getController();

        stage.setTitle("JavArena Dungeons");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (mainController != null) mainController.closeDatabase();
    }

    public static void main(String[] args) {
        launch();
    }
}