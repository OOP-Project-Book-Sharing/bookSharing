package com.example.first_draft.controller;

import com.example.first_draft.User;
import com.example.first_draft.UserDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;
    @FXML private Button togglePasswordBtn;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField locationField;
    @FXML private Label messageLabel;

    private UserDatabase userDB = new UserDatabase();
    private boolean isPasswordVisible = false;

    @FXML
    public void initialize() {
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());
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
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.isVisible() ? passwordField.getText() : passwordVisibleField.getText();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String location = locationField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username & password required");
            return;
        }

        if (username.length() > 32) {
            showError("Username must not exceed 32 characters.");
            return;
        }

        if (!email.isEmpty()) {
            if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.(com|org|net|edu|gov)$", email)) {
                showError("Invalid email format (must end with .com/.org/.net etc.)");
                return;
            }
        } else {
            showError("Email is required.");
            return;
        }

        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,}$", password)) {
            showError("Weak password! Needs upper, lower, digit, special char, 8+ length.");
            return;
        }

        if (!phone.isEmpty()) {
            if (!Pattern.matches("^01\\d{9}$", phone)) {
                showError("Invalid phone (must be 11 digits starting with 01).");
                return;
            }
        } else {
            showError("Phone number required.");
            return;
        }

        if (location.isEmpty()) {
            showError("Location is required.");
            return;
        }

        if (userDB.userExists(username)) {
            showError("Username already exists!");
            return;
        }

        if (userDB.emailExists(email)) {
            showError("Email already registered!");
            return;
        }

        userDB.addUser(new User(username, password, email, phone, location));
        showSuccess("User registered successfully!");
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("message-error");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("message-success");
    }

    @FXML
    private void handleLoginRedirect(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
