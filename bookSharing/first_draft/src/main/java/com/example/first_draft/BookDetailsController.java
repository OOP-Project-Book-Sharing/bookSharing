package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
    private List<Book> books;
    private int currentIndex = 0;

    private static final double IMAGE_WIDTH = 200;
    private static final double IMAGE_HEIGHT = 252;

    @FXML
    public void initialize() {
        buyAmount.setOnAction(event -> handlePayment("Buy", buyAmount.getText()));
        rentAmount.setOnAction(event -> handlePayment("Rent", rentAmount.getText()));
        nextBookButton.setOnAction(event -> loadNextBook());

        // Ensure fixed image size
        bookImage.setFitWidth(IMAGE_WIDTH);
        bookImage.setFitHeight(IMAGE_HEIGHT);
        bookImage.setPreserveRatio(true);
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

        currentIndex = (currentIndex + 1) % books.size();
        setBookDetails(books.get(currentIndex));
    }

    public void setBookDetails(Book book) {
        if (book == null) return;

        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        descriptionArea.setText("Summary: " + book.getDescription());

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

        // Load image
        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            bookImage.setImage(book.getCover().getImage());
        }
    }

    private void handlePayment(String actionType, String amountText) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(actionType + " Payment");
        dialog.setHeaderText("Enter your password to complete " + actionType.toLowerCase() + " payment");

        // Styled dialog content
        Label label = new Label("Password:");
        label.setFont(new Font("Arial", 14));

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200);

        VBox content = new VBox(10);
        content.getChildren().addAll(label, passwordField);
        content.setMinWidth(300);
        dialog.getDialogPane().setContent(content);

        // Buttons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Result converter
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(enteredPassword -> {
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
        });
    }
}
