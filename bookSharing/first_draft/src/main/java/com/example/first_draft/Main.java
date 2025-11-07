package com.example.first_draft;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("login.fxml")
        );

        if (loader.getLocation() == null) {
            throw new RuntimeException("ERROR: Cannot find login.fxml. Make sure it is under src/main/resources/");
        }

        Scene scene = new Scene(loader.load(), 600, 500);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
