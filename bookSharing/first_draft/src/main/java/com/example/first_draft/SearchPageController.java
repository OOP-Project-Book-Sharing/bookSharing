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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchPageController{

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField searchTextField;

    private List<Book> ListOfBooks;

    public void setBooks(List<Book> books) {
        this.ListOfBooks = books;
    }

    @FXML
    public void search(ActionEvent event){
        gridPane.getChildren().clear();
        ArrayList<Book> searchResults = searchlist(searchTextField.getText());
        gridPane.getChildren().clear();

        if (searchResults == null || searchResults.isEmpty()) {
            gridPane.add(new Label("No books available"), 1, 1);
            return;
        }

        for (int i = 0; i < searchResults.size(); i++) {
            Book book = searchResults.get(i);

            Label title = new Label(book.getTitle());
            Label author = new Label(book.getAuthor());
            ImageView cover = book.getCover();
            cover.setFitWidth(120);
            cover.setFitHeight(140);
            cover.setPreserveRatio(true);

            Button detailsButton = new Button("Details");

            VBox vbox = new VBox(5, cover, title, author, detailsButton);
            gridPane.add(vbox, i % 3, i / 3);
        }

    }

    public void displayBooks() {
        gridPane.getChildren().clear();
        int columns = 3;
        int col = 0, row = 0;

        if (ListOfBooks == null || ListOfBooks.isEmpty()) {
            gridPane.add(new Label("No books available"), 0, 0);
            return;
        }

        for (int i = 0; i < ListOfBooks.size(); i++) {
            Book book = ListOfBooks.get(i);

            Label title = new Label(book.getTitle());
            Label author = new Label(book.getAuthor());
            ImageView cover = book.getCover();
            cover.setFitWidth(120);
            cover.setFitHeight(160);
            cover.setPreserveRatio(true);

            // --- Availability / Type label ---
            Label statusLabel = new Label();
            if (book.getBuyAmount() < 0 && book.getRentAmount() < 0) {
                statusLabel.setText("Not for Sale or Rent");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            } else if (book.getBuyAmount() < 0) {
                statusLabel.setText("For Rent Only");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            } else if (book.getRentAmount() < 0) {
                statusLabel.setText("For Sale Only");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            } else {
                statusLabel.setText("For Sale & Rent");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            }

            // --- See Details button ---
            Button detailsButton = new Button("See Details");
            detailsButton.setOnAction(event -> openBookDetails(book));

            // --- VBox layout for each book ---
            VBox vbox = new VBox(8);
            vbox.setAlignment(Pos.CENTER);
            vbox.setPadding(new Insets(10));
            vbox.setPrefWidth(150);
            vbox.setPrefHeight(170);
            vbox.getChildren().addAll(cover, title, author, statusLabel, detailsButton);

            vbox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; "
                    + "-fx-border-radius: 6; -fx-background-radius: 6;");

            gridPane.add(vbox, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }

        }
    }

    public ArrayList<Book> searchlist(String searchKeyword){
        ArrayList<Book> searchResults = new ArrayList<>();

        for (Book book : ListOfBooks) {
            if (book.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(searchKeyword.toLowerCase())) {

                searchResults.add(book);
            }
        }
        return searchResults;

    }

    private void openBookDetails(Book selectedBook) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("bookDetails.fxml"));
            Parent root = loader.load();

            BookDetailsController controller = loader.getController();
            controller.setBooks(ListOfBooks);
            controller.setBookDetails(selectedBook);

            Stage stage = (Stage) gridPane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(selectedBook.getTitle() + " - Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
