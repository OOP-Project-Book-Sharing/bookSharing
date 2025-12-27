package com.example.first_draft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private Button togglePasswordBtn;
    @FXML private Label messageLabel;

    private final UserDatabase userDB = new UserDatabase();
    private boolean isPasswordVisible = false;

    @FXML
    public void initialize() {
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());

        // Add Enter key support for login
        usernameField.setOnAction(event -> {
            try {
                handleLogin(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        passwordField.setOnAction(event -> {
            try {
                handleLogin(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        passwordVisibleField.setOnAction(event -> {
            try {
                handleLogin(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        togglePasswordBtn.setOnAction(event -> {
            if (isPasswordVisible) { // Hide password
                passwordVisibleField.setVisible(false);
                passwordVisibleField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                togglePasswordBtn.setText("👁"); // closed eye
            } else { // Show password
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                passwordVisibleField.setVisible(true);
                passwordVisibleField.setManaged(true);
                togglePasswordBtn.setText("👁🗨"); // open eye
            }
            isPasswordVisible = !isPasswordVisible;
        });
    }
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.isVisible() ? passwordField.getText() : passwordVisibleField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username & password required");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        if (userDB.validateUser(username, password)) {
            //loggedInUsername = username; // store logged-in user
            messageLabel.setText("Login successful!");
            messageLabel.setStyle("-fx-text-fill: #00ff99;");
            goToMainLayout(event, username);
        } else {
            messageLabel.setText("Invalid username or password");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
    private void goToMainLayout(ActionEvent event, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/mainLayout.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);

        MainLayoutController controller = loader.getController();
        controller.setCurrentUser(username);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("BookPanda");
        stage.setResizable(false);

        Image icon = new Image(getClass().getResourceAsStream("images/logo.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void handleRegisterRedirect(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
            stage.show();
        } catch (Exception e) {
            System.out.println("Cannot load register.fxml");
            e.printStackTrace();
        }
    }
}