package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;
import java.util.regex.Pattern;

public class UpdateAccountPopupController {

    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField locationField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private User currentUser;
    private UserDatabase userDB = new UserDatabase();

    public void setUser(User user) {
        this.currentUser = user;

        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhone());
        locationField.setText(user.getLocation());
        passwordField.setText(user.getPassword());
    }

    @FXML
    private void handleUpdateAccount() {
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String location = locationField.getText().trim();
        String password = passwordField.getText().trim();

        // VALIDATIONS
        if (password.isEmpty()) {
            messageLabel.setText("Password required");
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

        if (userDB.emailExists(email) && email.compareTo(currentUser.getEmail()) != 0) {
            messageLabel.setText("Email already registered!");
            return;
        }

        // UPDATE USER IN DATABASE LIST
        List<User> users = userDB.getUsers();
        for (User u : users) {
            if (u.getUsername().equals(currentUser.getUsername())) {
                u.setEmail(email);
                u.setPhone(phone);
                u.setLocation(location);
                u.setPassword(password);
                break;
            }
        }

        // SAVE DATABASE
        userDB.save();

        // CLOSE POPUP
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();

        // Reload the account page through SceneManager
        SceneManager.switchView("accountPage.fxml");
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.close();
    }
}
