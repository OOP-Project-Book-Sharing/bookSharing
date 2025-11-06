package com.example.first_draft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GenreController {

    @FXML
    private VBox vbox;

    private List<Genre> genres;

    // called from the main app or previous controller
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        loadGenres(); // populate buttons
    }

    public void loadGenres() {
        vbox.getChildren().clear();

        for (Genre g : genres) {
            Button genreButton = new Button(g.getName());
            genreButton.setPrefWidth(200);
            genreButton.setOnAction(event -> {
                try {
                    switchToViewBooks(g, event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            vbox.getChildren().add(genreButton);
        }
    }

    private void switchToViewBooks(Genre selectedGenre, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homePage.fxml"));
        Parent root = fxmlLoader.load();

        // Get HomePageController
        HomePageController homeController = fxmlLoader.getController();
        homeController.setBooks(selectedGenre.getBooks()); // set the book list
        homeController.displayBooks(); // show books

        // Switch the scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
