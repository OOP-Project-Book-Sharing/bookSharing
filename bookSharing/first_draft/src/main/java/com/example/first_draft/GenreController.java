package com.example.first_draft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class GenreController {

    @FXML
    private VBox vbox;

    private List<Genre> genres;

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        loadGenres();
    }

    public void loadGenres() {
        vbox.getChildren().clear();
        vbox.setAlignment(Pos.TOP_LEFT); // align all content from left

        for (Genre g : genres) {
            // Genre name button
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

            // Create a horizontal box for book previews + "More" button
            HBox displayBox = new HBox(20);
            displayBox.setAlignment(Pos.CENTER_LEFT);
            displayBox.setPadding(new Insets(10, 0, 10, 30)); // left padding for visual margin

            // limit preview to first 2 books (or available count)
            int previewCount = Math.min(2, booksInGenre.size());
            for (int i = 0; i < previewCount; i++) {
                Book book = booksInGenre.get(i);
                VBox bookBox = new VBox(5);
                bookBox.setAlignment(Pos.TOP_CENTER);
                bookBox.getStyleClass().add("book-box");

                ImageView cover = book.getCover();
                cover.setFitWidth(110);
                cover.setFitHeight(130);
                cover.setPreserveRatio(true);

                Label title = new Label(book.getTitle());
                title.getStyleClass().add("title");
                Label author = new Label(book.getAuthor());
                author.getStyleClass().add("author");

                bookBox.getChildren().addAll(cover, title, author);
                displayBox.getChildren().add(bookBox);
            }

            // Spacer between books and "More" button
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            displayBox.getChildren().add(spacer);


            Button moreButton = new Button("More Books →");
            moreButton.setOnAction(event -> {
                try {
                    switchToViewBooks(g, event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            moreButton.getStyleClass().add("more-button");
            displayBox.getChildren().add(moreButton);

            vbox.getChildren().add(displayBox);
        }
    }

    private void switchToViewBooks(Genre selectedGenre, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homePage.fxml"));
        Parent root = fxmlLoader.load();

        HomePageController homeController = fxmlLoader.getController();
        homeController.setBooks(selectedGenre.getBooks());
        homeController.displayBooks();

        if (root instanceof Region region) {
            region.prefWidthProperty().bind(SceneManager.getMainStackPane().widthProperty());
            region.prefHeightProperty().bind(SceneManager.getMainStackPane().heightProperty());
        }

        SceneManager.getMainStackPane().getChildren().setAll(root);
    }
}
