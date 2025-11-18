package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class AddBookController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextArea descField;
    @FXML private TextField buyField;
    @FXML private TextField rentField;
    @FXML private TextField genreField;
    @FXML private ImageView previewImage;
    @FXML private Button uploadButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private String username;
    private BookDatabase bookDatabase;
    private Book bookToEdit = null; // null = Add, non-null = Edit
    private File selectedImageFile = null;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBookDatabase(BookDatabase bookDatabase) {
        this.bookDatabase = bookDatabase;
    }

    /** Called by MyBooksController to pre-fill fields for editing */
    public void setBookToEdit(Book book) {
        this.bookToEdit = book;

        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        descField.setText(book.getDescription());
        buyField.setText(String.valueOf(book.getBuyAmount()));
        rentField.setText(String.valueOf(book.getRentAmount()));
        genreField.setText(book.getGenre());

        // Load preview image from relative path
        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            File file = new File(book.getImagePath());
            if (file.exists()) {
                previewImage.setImage(new Image(file.toURI().toString()));
            }
        }

        saveButton.setText("Save Changes");
    }

    @FXML
    public void initialize() {
        previewImage.setFitWidth(120);
        previewImage.setFitHeight(160);

        uploadButton.setOnAction(e -> chooseImage());
        saveButton.setOnAction(e -> saveBook());
        cancelButton.setOnAction(e -> closeWindow());
    }

    /** Opens a file chooser to select a cover image */
    private void chooseImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = chooser.showOpenDialog(previewImage.getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            previewImage.setImage(new Image(file.toURI().toString()));
        }
    }

    /** Save new book or update existing book */
    private void saveBook() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String desc = descField.getText().trim();
        String genre = genreField.getText().trim();
        int buy = buyField.getText().isEmpty() ? 0 : Integer.parseInt(buyField.getText());
        int rent = rentField.getText().isEmpty() ? 0 : Integer.parseInt(rentField.getText());

        // Save image to project images folder and use relative path
        String imagePath = "";
        try {
            if (selectedImageFile != null) {
                File imagesDir = new File("images");
                if (!imagesDir.exists()) imagesDir.mkdirs();

                String destFileName = System.currentTimeMillis() + "_" + selectedImageFile.getName();
                File destFile = new File(imagesDir, destFileName);

                java.nio.file.Files.copy(
                        selectedImageFile.toPath(),
                        destFile.toPath(),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                imagePath = "images/" + destFileName;
            } else if (bookToEdit != null) {
                imagePath = bookToEdit.getImagePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (title.isEmpty() || author.isEmpty()) {
            System.out.println("Title and Author are required");
            return;
        }

        if (bookToEdit != null) {
            // Update existing book
            bookToEdit.setTitle(title);
            bookToEdit.setAuthor(author);
            bookToEdit.setDescription(desc);
            bookToEdit.setGenre(genre);
            bookToEdit.setBuyAmount(buy);
            bookToEdit.setRentAmount(rent);
            bookToEdit.setImagePath(imagePath);

            bookDatabase.updateBook(bookToEdit);
        } else {
            // Add new book
            Book newBook = new Book(title, author, desc, imagePath, buy, rent, true,
                    username, null, null, genre);
            bookDatabase.addBook(newBook);
        }

        if (!GenreDatabase.loadGenres().contains(genre)) {
            GenreDatabase.addGenre(genre);
        }

        closeWindow();
    }

    /** Closes the window */
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
