package com.example.first_draft;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/fxml/login.fxml"));
            if (loader.getLocation() == null) {
                throw new RuntimeException("ERROR: Cannot find login.fxml. Application not running");
            }

            Scene scene = new Scene(loader.load(), 800, 600);
            stage.setScene(scene);
            stage.setTitle("Login");
            Image icon = new Image(getClass().getResourceAsStream("/com/example/first_draft/images/logo.png"));
            stage.getIcons().add(icon);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace(); //stack trace of the exception — i.e., it shows where and why the error happened in your code.

            Alert alert = new Alert(
                    Alert.AlertType.ERROR,
                    "Startup failed: " + e.getMessage()
            );
            alert.showAndWait(); //user and wait for their response before continuing program execution.
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
