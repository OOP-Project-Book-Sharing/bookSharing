package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import java.util.List;
import java.util.Optional;

public class BookDetailsController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label descriptionArea;

    @FXML
    private ImageView bookImage;

    @FXML
    private Button buyAmount;

    @FXML
    private Button rentAmount;

    @FXML
    private Button nextBookButton;

    private String paymentPassword = "1234"; // Example password
    private List<Book> books;                // List of books
    private int currentIndex = 0;            // Track which book is currently shown

    @FXML
    public void initialize() {
        buyAmount.setOnAction(event -> handlePayment("Buy", buyAmount.getText()));
        rentAmount.setOnAction(event -> handlePayment("Rent", rentAmount.getText()));
        nextBookButton.setOnAction(event -> loadNextBook());
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        if (books != null && !books.isEmpty()) {
            currentIndex = 0;
            setBookDetails(books.get(currentIndex));
        }
    }

    private void loadNextBook() {
        if (books == null || books.isEmpty()) return;

        // Move to next book, loop back to first if at the end
        currentIndex = (currentIndex + 1) % books.size();
        setBookDetails(books.get(currentIndex));
    }

    public void setBookDetails(Book book) {
        if (book == null) return;

        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        descriptionArea.setText("Summary: " + book.getDescription());

        // --- Set button text and disable based on value ---
        if (book.getBuyAmount() < 0) {
            buyAmount.setText("Not for Sale");
            buyAmount.setDisable(true);
            buyAmount.setStyle("-fx-opacity: 0.5;");
        } else {
            buyAmount.setText("Buy: $" + book.getBuyAmount());
            buyAmount.setDisable(false);
            buyAmount.setStyle("-fx-opacity: 1.0;");
        }

        if (book.getRentAmount() < 0) {
            rentAmount.setText("Not for Rent");
            rentAmount.setDisable(true);
            rentAmount.setStyle("-fx-opacity: 0.5;");
        } else {
            rentAmount.setText("Rent: $" + book.getRentAmount());
            rentAmount.setDisable(false);
            rentAmount.setStyle("-fx-opacity: 1.0;");
        }

        // --- Set image ---
        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            bookImage.setImage(book.getCover().getImage());
        }
    }

    private void handlePayment(String actionType, String amountText) {
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle(actionType + " Payment");
        passwordDialog.setHeaderText("Enter your password to complete " + actionType.toLowerCase() + " payment");
        passwordDialog.setContentText("Password:");

        Optional<String> result = passwordDialog.showAndWait();
        if (result.isPresent()) {
            String enteredPassword = result.get();
            if (enteredPassword.equals(paymentPassword)) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Payment Successful");
                success.setHeaderText(null);
                success.setContentText(actionType + " payment completed successfully (" + amountText + ")");
                success.showAndWait();
            } else {
                Alert denied = new Alert(Alert.AlertType.ERROR);
                denied.setTitle("Payment Denied");
                denied.setHeaderText(null);
                denied.setContentText("Incorrect password! Payment denied.");
                denied.showAndWait();
            }
        }
    }
}
