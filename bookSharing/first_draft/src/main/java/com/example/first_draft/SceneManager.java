package com.example.first_draft;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class SceneManager {
    private static StackPane mainStackPane;

    public static void setMainStackPane(StackPane pane) {
        mainStackPane = pane;
    }

    public static StackPane getMainStackPane() {
        return mainStackPane;
    }

    public static void switchView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            mainStackPane.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
