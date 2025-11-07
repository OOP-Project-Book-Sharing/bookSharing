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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Username & password required");
            return;
        }

        if (validateUser(username, password)) {
            goToHome(event);
        } else {
            messageLabel.setText("Invalid username or password");
        }
    }

    private boolean validateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                if (arr.length >= 2) {
                    if (arr[0].equals(username) && arr[1].equals(password)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading users.txt");
            e.printStackTrace();
        }
        return false;
    }

    private void goToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/mainLayout.fxml"));
        Scene scene = new Scene(loader.load(), 600, 500);

        // ✅ Create book lists
        List<Book> booksDetective = new ArrayList<>();
        List<Book> booksThriller = new ArrayList<>();

        Book book1 = new Book("Death on the Nile", "Agatha Christie", "Fantasy adventure story.",
                "/com/example/first_draft/images/book1.jpeg", -1, 150, true);
        Book book2 = new Book("The Murder of Roger Ackroyd", "Agatha Christie",
                "A classic detective novel.",
                "/com/example/first_draft/images/roger_ackroyd.jpg", 250, -1, true);
        Book book3 = new Book("Byokmesh", "Agatha Christie",
                "A classic detective novel.",
                "/com/example/first_draft/images/byokmesh.jpg", 250, 100, true);
        Book book4 = new Book("Opshora theatre er mamla", "Agatha Christie",
                "A classic detective novel.",
                "/com/example/first_draft/images/feluda.jpg", -1, 100, true);
        Book book5 = new Book("And there were none", "Agatha Christie",
                "A classic detective novel.",
                "/com/example/first_draft/images/poirot.jpg", 250, 100, true);

        booksDetective.addAll(Arrays.asList(book1, book2));
        booksThriller.addAll(Arrays.asList(book3, book4));

        // ✅ Create genres
        Genre genre1 = new Genre("Detective", booksDetective);
        Genre genre2 = new Genre("Thriller", booksThriller);

        // ✅ Get main layout controller
        MainLayoutController controller = loader.getController();

        // ✅ Pass both lists to controller (order doesn’t matter now)
        controller.setBooks(Arrays.asList(book1, book2, book3, book4));
        controller.setGenres(Arrays.asList(genre1, genre2));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle("📚 Book Sharing App");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRegisterRedirect(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            System.out.println("Cannot load register.fxml");
            e.printStackTrace();
        }
    }

}
