package com.example.first_draft.controller;

import com.example.first_draft.Book;
import com.example.first_draft.BookDatabase;
import com.example.first_draft.cart.Cart;
import com.example.first_draft.cart.CartItem;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class BookDetailsController {

    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Label descriptionArea;
    @FXML private ImageView bookImage;
    @FXML private Button buyAmount;
    @FXML private Button rentAmount;
    @FXML private Label ownerLabel;
    @FXML private Label genreLabel;

    Book book;
    private static final double IMAGE_WIDTH = 200;
    private static final double IMAGE_HEIGHT = 252;

    private String currentUser;

    private BookDatabase bookDatabase = new BookDatabase();

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    public void initialize() {
        bookImage.setFitWidth(IMAGE_WIDTH);
        bookImage.setFitHeight(IMAGE_HEIGHT);
        bookImage.setPreserveRatio(true);

        buyAmount.setOnAction(e -> handleBuy());
        rentAmount.setOnAction(e -> handleRent());
    }

    public void setBook(Book book) {
        this.book = book;
        setBookDetails();
    }

    public void setBookDetails() {
        if (book == null) return;

        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        descriptionArea.setText(book.getDescription());
        ownerLabel.setText(book.getOwner());
        genreLabel.setText(book.getGenre());

        if (book.getBuyAmount() <= 0) {
            buyAmount.setText("Not for Sale");
            buyAmount.setDisable(true);
            buyAmount.setStyle("-fx-opacity: 0.5;");
        } else {
            buyAmount.setText("Buy: $" + book.getBuyAmount());
            buyAmount.setDisable(false);
            buyAmount.setStyle("-fx-opacity: 1.0;");
        }

        if (book.getRentAmount() <= 0) {
            rentAmount.setText("Not for Rent");
            rentAmount.setDisable(true);
            rentAmount.setStyle("-fx-opacity: 0.5;");
        } else {
            rentAmount.setText("Rent: $" + book.getRentAmount());
            rentAmount.setDisable(false);
            rentAmount.setStyle("-fx-opacity: 1.0;");
        }

        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            bookImage.setImage(book.getCover().getImage());
        }
    }

    private void handleBuy() {
        // Check if user is trying to buy their own book
        if (currentUser != null && book.getOwner().equalsIgnoreCase(currentUser)) {
            showAlert(Alert.AlertType.ERROR, "You cannot buy your own book.");
            return;
        }

        // Check if book is available
        if (!book.isAvailable() || (book.getRentedTo() != null && !book.getRentedTo().isEmpty())) {
            showAlert(Alert.AlertType.ERROR, "This book is not available.");
            return;
        }

        // Add book to cart with BUY action
        CartItem cartItem = new CartItem(book, CartItem.ActionType.BUY);
        Cart.getInstance().addItem(cartItem);
        showAlert(Alert.AlertType.INFORMATION, "Book added to cart for buying! (Price: $" + book.getBuyAmount() + ")");
    }

    private void handleRent() {
        // Check if user is trying to rent their own book
        if (currentUser != null && book.getOwner().equalsIgnoreCase(currentUser)) {
            showAlert(Alert.AlertType.ERROR, "You cannot rent your own book.");
            return;
        }

        // Check if book is available
        if (!book.isAvailable() || (book.getRentedTo() != null && !book.getRentedTo().isEmpty())) {
            showAlert(Alert.AlertType.ERROR, "This book is not available.");
            return;
        }

        // Prompt for rental duration with date picker
        Dialog<LocalDate> dateDialog = new Dialog<>();
        dateDialog.setTitle("Select Rental Period");
        dateDialog.setHeaderText("Choose how long you want to rent \"" + book.getTitle() + "\"");

        DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(7));
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now().plusDays(1)));
            }
        });

        VBox dateContent = new VBox(10, new Label("Return by date:"), datePicker);
        dateContent.setMinWidth(300);
        dateContent.setPadding(new Insets(10));
        dateDialog.getDialogPane().setContent(dateContent);
        dateDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dateDialog.setResultConverter(btn -> btn == ButtonType.OK ? datePicker.getValue() : null);

        Optional<LocalDate> dateResult = dateDialog.showAndWait();
        if (dateResult.isEmpty()) return;

        LocalDate dueDate = dateResult.get();
        long days = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);

        if (days <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid rental period selected.");
            return;
        }

        int totalCost = (int) (days * book.getRentAmount());

        // Add book to cart with RENT action and rental days
        CartItem cartItem = new CartItem(book, CartItem.ActionType.RENT, (int) days, dueDate);
        Cart.getInstance().addItem(cartItem);
        showAlert(Alert.AlertType.INFORMATION,
            "Book added to cart for renting!\n" + days + " days - Total: $" + totalCost);
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
