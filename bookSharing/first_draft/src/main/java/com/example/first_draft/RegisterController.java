package com.example.first_draft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField locationField;
    @FXML private Label messageLabel;

    private UserDatabase userDB = new UserDatabase();

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String location = locationField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username & password required");
            return;
        }

        if (username.length() > 32) {
            messageLabel.setText("Username must not exceed 32 characters.");
            return;
        }

        if (!email.isEmpty()) {
            if (!Pattern.matches("^[\\w.-]+@[\\w.-]+\\.(com|org|net|edu|gov)$", email)) {
                messageLabel.setText("Invalid email format (must end with .com/.org/.net etc.)");
                return;
            }
        } else {
            messageLabel.setText("Email is required.");
            return;
        }

        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{8,}$", password)) {
            messageLabel.setText("Weak password! Needs upper, lower, digit, special char, 8+ length.");
            return;
        }

        if (!phone.isEmpty()) {
            if (!Pattern.matches("^01\\d{9}$", phone)) {
                messageLabel.setText("Invalid phone (must be 11 digits starting with 01).");
                return;
            }
        } else {
            messageLabel.setText("Phone number required.");
            return;
        }

        if (location.isEmpty()) {
            messageLabel.setText("Location is required.");
            return;
        }

        if (userDB.userExists(username)) {
            messageLabel.setText("Username already exists!");
            return;
        }

        if (userDB.emailExists(email)) {
            messageLabel.setText("Email already registered!");
            return;
        }

        userDB.addUser(new User(username, password, email, phone, location));
        messageLabel.setText("User registered successfully!");
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
