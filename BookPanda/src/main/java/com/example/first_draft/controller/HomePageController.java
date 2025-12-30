package com.example.first_draft.controller;

import com.example.first_draft.Book;
import com.example.first_draft.GenreDatabase;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class HomePageController {

    @FXML
    private VBox vbox;

    private List<Book> allBooks;

    private String username;

    public void setCurrentUser(String currentUser) {
        this.username = currentUser;
    }

    public void setBooks(List<Book> allBooks) {
        this.allBooks = allBooks;
    }

    @FXML
    public void displayBooks() {
        vbox.getChildren().clear();
        vbox.setAlignment(Pos.TOP_LEFT);

        List<String> genreNames = GenreDatabase.loadGenres();

        for (String genreName : genreNames) {
            List<Book> booksInGenre = allBooks.stream()
                    .filter(b -> genreName.equalsIgnoreCase(b.getGenre()))
                    .filter(b -> !b.getOwner().equalsIgnoreCase(username)) // not my book
                    .filter(Book::isAvailable)                             // must be available
                    .filter(b -> b.getRentedTo() == null || b.getRentedTo().isEmpty()) // not rented
                    .collect(Collectors.toList());

            if (booksInGenre.isEmpty())
                continue;

            Button genreButton = new Button(genreName);
            genreButton.setPrefWidth(200);

            // Filter books for this genre



            genreButton.setOnAction(event -> {
                try {
                    switchToViewBooks(booksInGenre);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            vbox.getChildren().add(genreButton);

            // Horizontal box for book previews
            HBox displayBox = new HBox(20);
            displayBox.setAlignment(Pos.CENTER_LEFT);
            displayBox.setPadding(new Insets(10, 0, 10, 30));

            int previewCount = Math.min(5, booksInGenre.size());
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

                // Add click handler to show book details
                bookBox.setOnMouseClicked(event -> showBookDetails(book));
                bookBox.setStyle(bookBox.getStyle() + "-fx-cursor: hand;");

                displayBox.getChildren().add(bookBox);
            }


            Button moreButton = new Button("More Books →");
            moreButton.setOnAction(event -> {
                switchToViewBooks(booksInGenre);
            });
            moreButton.getStyleClass().add("more-button");
            displayBox.getChildren().add(moreButton);

            vbox.getChildren().add(displayBox);
        }
    }

    private void switchToViewBooks(List<Book> booksInGenre) {
        SceneManager.switchViewWithData("/com/example/first_draft/fxml/searchPage.fxml", controller -> {
            if (controller instanceof SearchPageController searchCtrl) {
                searchCtrl.setUsername(username);
                searchCtrl.setBooks(booksInGenre);
                searchCtrl.displayBooks();
            }
        });
    }

    private void showBookDetails(Book book) {
        SceneManager.switchViewWithData("/com/example/first_draft/fxml/bookDetails.fxml", controller -> {
            if (controller instanceof BookDetailsController bookCtrl) {
                bookCtrl.setCurrentUser(username);
                bookCtrl.setBook(book);
            }
        });
    }
}
