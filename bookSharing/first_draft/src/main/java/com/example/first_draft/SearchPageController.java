package com.example.first_draft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchPageController {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField searchTextField;

    private List<Book> books;
    private String username;

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void displayBooks() {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        int columns = 3;
        int col = 0, row = 0;

        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / columns);
            gridPane.getColumnConstraints().add(cc);
        }

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);

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
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setPadding(new Insets(10));
            vbox.setPrefWidth(160);
            vbox.setPrefHeight(270);
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

    @FXML
    public void search(ActionEvent e){
        //gridPane.getChildren().clear();
        if (searchTextField.getText().isEmpty()) {
            displayBooks();
            return;
        }
        ArrayList<Book> searchResults = searchlist(searchTextField.getText());

        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        int columns = 3;
        int col = 0, row = 0;

        for (int i = 0; i < columns; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / columns);
            gridPane.getColumnConstraints().add(cc);
        }

        for (int i = 0; i < searchResults.size(); i++) {
            Book book = searchResults.get(i);

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
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setPadding(new Insets(10));
            vbox.setPrefWidth(160);
            vbox.setPrefHeight(270);
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

        for (Book book : books) {

            if (book.getOwner().equalsIgnoreCase(username)) continue;
            if (!book.isAvailable()) continue;
            if (book.getRentedTo() != null && !book.getRentedTo().isEmpty()) continue;

            if (book.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(searchKeyword.toLowerCase())) {

                searchResults.add(book);
            }
        }
        return searchResults;
    }

    private void openBookDetails(Book book) {
        SceneManager.switchViewWithData("/com/example/first_draft/bookDetails.fxml", controller -> {
            if (controller instanceof BookDetailsController c) {
                c.setBook(book);
                c.setCurrentUser(username);
            }
        });
    }

    public List<Book> getBooks() {
        return books;
    }

}
