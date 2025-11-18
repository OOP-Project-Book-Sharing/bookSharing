package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MainLayoutController {

    @FXML private StackPane mainStackPane;
    @FXML private Button homeButton;
    @FXML private Button myBooksButton;
    @FXML private Button searchButton;
    @FXML private Button accountButton;
    @FXML private Button chatButton;
    @FXML private Button prevButton;
    @FXML private Button logoutButton;

    private String currentUser;

    public void setCurrentUser(String user) {
        this.currentUser = user;
        SceneManager.setCurrentUser(user);
        loadFirstPage();
    }

    @FXML
    public void initialize() {
        SceneManager.setMainStackPane(mainStackPane);

        homeButton.setOnAction(e -> SceneManager.switchView("/com/example/first_draft/homePage.fxml"));
        myBooksButton.setOnAction(e -> SceneManager.switchView("/com/example/first_draft/myBooks.fxml"));
        searchButton.setOnAction(e -> SceneManager.switchView("/com/example/first_draft/searchPage.fxml"));
        accountButton.setOnAction(e -> SceneManager.switchView("/com/example/first_draft/accountPage.fxml"));
        chatButton.setOnAction(e -> SceneManager.switchView("/com/example/first_draft/chatPage.fxml"));

        prevButton.setOnAction(e -> SceneManager.goBack());

        if (logoutButton != null) {
            logoutButton.setOnAction(e -> logout());
        }
    }

    private void loadFirstPage() {
        if (currentUser != null && mainStackPane != null && mainStackPane.getChildren().isEmpty()) {
            SceneManager.switchView("/com/example/first_draft/homePage.fxml");
        }
    }

    private void logout() {
        try {
            SceneManager.clearHistory(); // Clear navigation history
            Stage stage = (Stage) mainStackPane.getScene().getWindow();
            stage.close();

            // Open login page in new stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
