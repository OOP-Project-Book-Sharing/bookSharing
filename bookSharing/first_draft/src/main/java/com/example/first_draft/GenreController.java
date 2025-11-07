package com.example.first_draft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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
            List<Book> booksInGenre = g.getBooks();
            HBox displayBox = new HBox();

            VBox book1 = new VBox();
            Label name1 = new Label(booksInGenre.get(0).getTitle());
            Label author1 = new Label(booksInGenre.get(0).getAuthor());
            ImageView cover1 = booksInGenre.get(0).getCover();
            cover1.setFitWidth(110);
            cover1.setFitHeight(130);
            cover1.setPreserveRatio(true);
            book1.getChildren().addAll(cover1, name1, author1);
            book1.setAlignment(Pos.TOP_CENTER);
            displayBox.getChildren().add(book1);

            VBox book2 = new VBox();
            Label name2 = new Label(booksInGenre.get(1).getTitle());
            Label author2 = new Label(booksInGenre.get(1).getAuthor());
            ImageView cover2 = booksInGenre.get(1).getCover();
            cover2.setFitWidth(110);
            cover2.setFitHeight(130);
            cover2.setPreserveRatio(true);
            book2.getChildren().addAll(cover2, name2, author2);
            book2.setAlignment(Pos.TOP_CENTER);
            displayBox.getChildren().add(book2);
            displayBox.spacingProperty().bind(displayBox.widthProperty().multiply(0.05));
            displayBox.setAlignment(Pos.CENTER);

//            VBox book3 = new VBox();
//            Label name3 = new Label(booksInGenre.get(2).getTitle());
//            Label author3 = new Label(booksInGenre.get(2).getAuthor());
//            ImageView cover3 = booksInGenre.get(2).getCover();
//            cover3.setFitWidth(120);
//            cover3.setFitHeight(160);
//            cover3.setPreserveRatio(true);
//            book1.getChildren().addAll(cover3, name3, author3);
//            displayBox.getChildren().add(book3);
            vbox.getChildren().add(displayBox);
            vbox.setAlignment(Pos.TOP_CENTER);

        }
    }

    private void switchToViewBooks(Genre selectedGenre, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homePage.fxml"));
        Parent root = fxmlLoader.load();

        // Get HomePageController
        HomePageController homeController = fxmlLoader.getController();
        homeController.setBooks(selectedGenre.getBooks()); // set the book list
        homeController.displayBooks(); // show books

        if (root instanceof Region region) {
            region.prefWidthProperty().bind(SceneManager.getMainStackPane().widthProperty());
            region.prefHeightProperty().bind(SceneManager.getMainStackPane().heightProperty());
        }

        // Switch the scene
        SceneManager.getMainStackPane().getChildren().setAll(root);
    }
}
