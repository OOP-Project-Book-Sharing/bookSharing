package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.List;

public class myBooksController {

    @FXML
    private Button btnForSaleRent;

    @FXML
    private Button btnRented;

    @FXML
    private Button btnBorrowed;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox bookListVBox;

    private BorderPane borderPane;

    private List<Book> books;

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void displayBooks() {
        if (books == null || books.isEmpty()) return;

        bookListVBox.getChildren().clear();

        for (Book book : books) {
            HBox bookRow = new HBox(20);
            bookRow.setPadding(new Insets(10));
            bookRow.setAlignment(Pos.CENTER_LEFT);
            bookRow.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");

            // Left: Details VBox
            VBox detailsVBox = new VBox(5);

            Label titleLabel = new Label("Title: " + book.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

            Label authorLabel = new Label("Author: " + book.getAuthor());
            Label descriptionLabel = new Label("Description: " + book.getDescription());

            // See Details button
            Button seeDetailsButton = new Button("See Details");
            seeDetailsButton.setOnAction(e -> openBookDetails(book));

            detailsVBox.getChildren().addAll(titleLabel, authorLabel, descriptionLabel, seeDetailsButton);

            // Right: ImageView
            ImageView cover = book.getCover();
            cover.setFitWidth(100);
            cover.setFitHeight(150);
            cover.setPreserveRatio(true);

            bookRow.getChildren().addAll(detailsVBox, cover);
            bookListVBox.getChildren().add(bookRow);
        }

        scrollPane.setFitToWidth(true);
    }

    @FXML
    public void initialize() {
        // Optional: handle menu buttons
        btnForSaleRent.setOnAction(e -> displayBooksForSaleRent());
        btnRented.setOnAction(e -> displayBooksRented());
        btnBorrowed.setOnAction(e -> displayBooksBorrowed());
    }

    private void displayBooksForSaleRent() {
        displayBooks();
    }

    private void displayBooksRented() {
        displayBooks();
    }

    private void displayBooksBorrowed() {
        displayBooks();
    }

    // Open book details page
    private void openBookDetails(Book selectedBook) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bookDetails.fxml"));
            Parent root = loader.load();

            BookDetailsController controller = loader.getController();
            controller.setBooks(books);
            controller.setBookDetails(selectedBook);

            Stage stage = (Stage) bookListVBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(selectedBook.getTitle() + " - Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
