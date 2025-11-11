package com.example.first_draft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private UserDatabase userDB = new UserDatabase();
    private BookDatabase bookDB = new BookDatabase();

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username & password required");
            return;
        }

        if (userDB.validateUser(username, password)) {
            goToHome(event, username);
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }

    private void goToHome(ActionEvent event, String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/mainLayout.fxml"));
        Scene scene = new Scene(loader.load(), 800, 600);

        MainLayoutController controller = loader.getController();
        controller.setBookDatabase(bookDB);
        controller.setCurrentUser(username);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("📚 Book Sharing App");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRegisterRedirect(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("register.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            System.out.println("Cannot load register.fxml");
            e.printStackTrace();
        }
    }
}
