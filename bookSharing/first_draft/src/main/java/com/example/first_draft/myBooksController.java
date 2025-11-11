package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class myBooksController {

    @FXML private Button btnForSaleRent;
    @FXML private Button btnRented;
    @FXML private Button btnBorrowed;
    @FXML private Button btnAddBook;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox bookListVBox;

    private BookDatabase bookDatabase;
    private List<Book> books;

    @FXML
    public void initialize() {
        bookDatabase = new BookDatabase();
        books = bookDatabase.getAllBooks();

        btnForSaleRent.setOnAction(e -> displayBooks());
        btnRented.setOnAction(e -> displayBooksRented());
        btnBorrowed.setOnAction(e -> displayBooksBorrowed());
        btnAddBook.setOnAction(e -> openAddBookDialog());

        displayBooks(); // Show all by default
    }

    private interface BookFilter { boolean test(Book book); }

    public void displayBooks() { displayBooksFiltered(null); }

    private void displayBooksRented() { displayBooksFiltered(book -> !book.isAvailable()); }

    private void displayBooksBorrowed() { displayBooksFiltered(book -> book.getRentedTo() != null && !book.getRentedTo().isEmpty()); }

    private void displayBooksFiltered(BookFilter filter) {
        bookListVBox.getChildren().clear();

        for (Book book : books) {
            if (filter != null && !filter.test(book)) continue;

            HBox bookRow = new HBox(20);
            bookRow.setPadding(new Insets(10));
            bookRow.setAlignment(Pos.CENTER_LEFT);
            bookRow.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");

            VBox detailsVBox = new VBox(5);

            Label titleLabel = new Label("Title: " + book.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
            Label authorLabel = new Label("Author: " + book.getAuthor());
            Label descLabel = new Label("Description: " + book.getDescription());

            Button seeDetailsButton = new Button("See Details");
            seeDetailsButton.setOnAction(e -> openBookDetails(book));

            Button editButton = new Button("Edit Details");
            editButton.setOnAction(e -> openBookDialog(book, false));

            detailsVBox.getChildren().addAll(titleLabel, authorLabel, descLabel, seeDetailsButton, editButton);

            ImageView cover = book.getCover();
            cover.setFitWidth(100);
            cover.setFitHeight(150);
            cover.setPreserveRatio(true);

            bookRow.getChildren().addAll(detailsVBox, cover);
            bookListVBox.getChildren().add(bookRow);
        }

        scrollPane.setFitToWidth(true);
    }

    private void openBookDetails(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/bookDetails.fxml"));
            Parent page = loader.load();
            BookDetailsController controller = loader.getController();
            controller.setBooks(books);
            controller.setBookDetails(book);
            SceneManager.getMainStackPane().getChildren().setAll(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAddBookDialog() { openBookDialog(new Book(), true); }

    private void openBookDialog(Book book, boolean isNew) {
        Stage dialog = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());
        TextField descField = new TextField(book.getDescription());
        TextField buyField = new TextField(String.valueOf(book.getBuyAmount()));
        TextField rentField = new TextField(String.valueOf(book.getRentAmount()));
        TextField ownerField = new TextField(book.getOwner());
        TextField rentedToField = new TextField(book.getRentedTo());
        TextField dueDateField = new TextField(book.getDueDate());
        TextField genreField = new TextField(book.getGenre());

        // Image upload
        ImageView previewImage = new ImageView();
        previewImage.setFitWidth(100);
        previewImage.setFitHeight(150);
        previewImage.setPreserveRatio(true);

        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            try {
                previewImage.setImage(new javafx.scene.image.Image(new File(book.getImagePath()).toURI().toString()));
            } catch (Exception ignored) {}
        }

        Button uploadButton = new Button("Upload Image");
        uploadButton.setOnAction(e -> {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Select Book Cover");
            fileChooser.getExtensionFilters().addAll(
                    new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );

            java.io.File selectedFile = fileChooser.showOpenDialog(dialog);
            if (selectedFile != null) {
                book.setImagePath(selectedFile.getAbsolutePath()); // save path to book
                previewImage.setImage(new javafx.scene.image.Image(selectedFile.toURI().toString()));
            }
        });

        Button saveButton = new Button("Save");

        vbox.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Author:"), authorField,
                new Label("Description:"), descField,
                new Label("Buy Amount:"), buyField,
                new Label("Rent Amount:"), rentField,
                new Label("Owner:"), ownerField,
                new Label("Rented To:"), rentedToField,
                new Label("Due Date:"), dueDateField,
                new Label("Genre:"), genreField,
                new Label("Cover Image:"), previewImage, uploadButton,
                saveButton
        );

        dialog.setScene(new Scene(vbox));
        dialog.setTitle(isNew ? "Add New Book" : "Edit Book");
        dialog.show();

        saveButton.setOnAction(e -> {
            book.setTitle(titleField.getText());
            book.setAuthor(authorField.getText());
            book.setDescription(descField.getText());
            book.setBuyAmount(parseIntSafe(buyField.getText()));
            book.setRentAmount(parseIntSafe(rentField.getText()));
            book.setOwner(ownerField.getText());
            book.setRentedTo(rentedToField.getText());
            book.setDueDate(dueDateField.getText());
            book.setGenre(genreField.getText());

            if (isNew) bookDatabase.addBook(book);
            else bookDatabase.updateBook(book);

            updateGenreFile(book.getGenre());

            books = bookDatabase.getAllBooks();
            displayBooks();
            dialog.close();
        });
    }


    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return -1; }
    }

    private void updateGenreFile(String genre) {
        List<String> genres = GenreDatabase.loadGenres();
        if (!genres.contains(genre)) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("genres.txt", true))) {
                bw.write(genre);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
