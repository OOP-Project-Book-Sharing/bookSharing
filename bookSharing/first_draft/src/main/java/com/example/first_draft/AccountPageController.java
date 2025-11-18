package com.example.first_draft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AccountPageController {

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label locationLabel;
    @FXML private Label passwordLabel;

    private UserDatabase userDB = new UserDatabase();
    private String username;
    private User loggedInUser;

    public void setUsername(String username) {
        this.username = username;
        loadUserData(); // load data when username is set
    }

    /** Always reload user from DB */
    public void loadUserData() {
        if (username == null) return;

        loggedInUser = userDB.getUserByUsername(username);

        if (loggedInUser != null) {
            usernameLabel.setText(loggedInUser.getUsername());
            emailLabel.setText(loggedInUser.getEmail());
            phoneLabel.setText(loggedInUser.getPhone());
            locationLabel.setText(loggedInUser.getLocation());
            passwordLabel.setText("********");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        if (loggedInUser == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateAccountPopup.fxml"));
            Parent root = loader.load();

            UpdateAccountPopupController controller = loader.getController();
            controller.setUser(loggedInUser); // pass self for refreshing

            Stage popup = new Stage();
            popup.setTitle("Update Account");
            popup.setScene(new Scene(root));
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setResizable(false);
            popup.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
