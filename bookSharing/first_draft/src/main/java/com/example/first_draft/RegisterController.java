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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField locationField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String email    = emailField.getText().trim();
        String phone    = phoneField.getText().trim();
        String location = locationField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username & password required");
            return;
        }

        saveUser(username, password, email, phone, location);
        messageLabel.setText("User registered successfully!");
    }

    private void saveUser(String username, String password, String email, String phone, String location) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.txt", true))) {

            bw.write(username + "," + password + "," + email + "," + phone + "," + location);
            bw.newLine();

        } catch (IOException e) {
            messageLabel.setText("Error saving user!");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println("Cannot load login.fxml");
            e.printStackTrace();
        }
    }
}
