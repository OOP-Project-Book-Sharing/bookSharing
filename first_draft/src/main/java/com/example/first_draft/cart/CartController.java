package com.example.first_draft.cart;

import com.example.first_draft.Book;
import com.example.first_draft.BookDatabase;
import com.example.first_draft.UserDatabase;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.Optional;

public class CartController {

    @FXML private VBox itemsContainer;
    @FXML private Button purchaseAllButton;

    private String currentUser;
    private final BookDatabase bookDatabase = new BookDatabase();
    private final UserDatabase userDatabase = new UserDatabase();

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    public void initialize() {
        if (purchaseAllButton != null) {
            purchaseAllButton.setOnAction(e -> handlePurchaseAll());
        }
    }

    public void displayCart() {
        if (itemsContainer == null) return;
        itemsContainer.getChildren().clear();

        var cartItems = Cart.getInstance().getItems();

        if (cartItems.isEmpty()) {
            Label emptyMsg = new Label("Your cart is empty");
            emptyMsg.setStyle("-fx-font-size: 18px; -fx-text-fill: grey;");
            itemsContainer.getChildren().add(emptyMsg);
            purchaseAllButton.setDisable(true);
            return;
        }

        purchaseAllButton.setDisable(false);

        for (CartItem item : cartItems) {
            Book book = item.getBook();
            String action = item.getAction() == CartItem.ActionType.BUY ? "Buy" : "Rent";
            CartItem cartItem = item; // Create final reference for closure

            HBox bookRow = new HBox(20);
            bookRow.getStyleClass().add("book-row");
            bookRow.setPadding(new Insets(20));
            bookRow.setAlignment(Pos.TOP_LEFT);

            GridPane infoGrid = new GridPane();
            infoGrid.setHgap(20);
            infoGrid.setVgap(8);

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(45);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(40);
            infoGrid.getColumnConstraints().addAll(col1, col2);

            Label titleLabel = new Label("Title: " + book.getTitle());
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

            Label authorLabel = new Label("Author: " + book.getAuthor());
            Label genreLabel = new Label("Genre: " + book.getGenre());
            Label ownerLabel = new Label("Owner: " + book.getOwner());

            Label priceLabel;
            if (item.getAction() == CartItem.ActionType.BUY) {
                priceLabel = new Label("Price: $" + book.getBuyAmount());
            } else {
                int totalRentCost = item.getRentalDays() * book.getRentAmount();
                priceLabel = new Label("Rent: $" + book.getRentAmount() + "/day × " + item.getRentalDays() + " days = $" + totalRentCost);
            }
            priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
            priceLabel.setWrapText(true); // Allow text wrapping
            priceLabel.setMaxWidth(300); // Set maximum width

            infoGrid.add(titleLabel, 0, 0);
            infoGrid.add(authorLabel, 0, 1);
            infoGrid.add(genreLabel, 0, 2);
            infoGrid.add(ownerLabel, 0, 3);
            infoGrid.add(priceLabel, 1, 0);

            // Action buttons
            Button actionBtn = new Button(action);
            actionBtn.setStyle("-fx-padding: 8 20 8 20;");
            actionBtn.setOnAction(e -> handleAction(cartItem));

            Button removeBtn = new Button("Remove");
            removeBtn.setStyle("-fx-padding: 8 20 8 20;");
            removeBtn.setOnAction(e -> {
                Cart.getInstance().removeItem(cartItem);
                displayCart();
            });

            HBox buttonBox = new HBox(10, actionBtn, removeBtn);
            buttonBox.setAlignment(Pos.CENTER_LEFT);
            infoGrid.add(buttonBox, 0, 4, 2, 1);

            HBox.setHgrow(infoGrid, Priority.ALWAYS);

            // Add book cover image
            ImageView cover = book.getCover();
            cover.setFitWidth(100);
            cover.setFitHeight(130);
            cover.setPreserveRatio(true);

            bookRow.getChildren().addAll(infoGrid, cover);
            itemsContainer.getChildren().add(bookRow);
        }
    }

    private void handleAction(CartItem item) {
        Book book = item.getBook();
        if (item.getAction() == CartItem.ActionType.BUY) {
            handleBuy(book, item);
        } else {
            handleRent(book, item);
        }
    }

    private void handleBuy(Book book, CartItem item) {
        Dialog<String> passDialog = createPasswordDialog(
                "Buy Payment",
                "Enter your password to buy \"" + book.getTitle() + "\""
        );

        Optional<String> passResult = passDialog.showAndWait();
        if (passResult.isEmpty()) return;

        String password = passResult.get();
        if (!userDatabase.validateUser(currentUser, password)) {
            showAlert(Alert.AlertType.ERROR, "Incorrect password. Payment denied.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Purchase");
        confirm.setHeaderText(null);
        confirm.setContentText("Pay $" + book.getBuyAmount() + " to buy this book?");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        String originalOwner = book.getOwner();
        book.setOwner(currentUser);
        book.setAvailable(true);

        bookDatabase.deleteBook(book.getTitle(), originalOwner);
        bookDatabase.addBook(book);

        Cart.getInstance().removeItem(item);
        displayCart();

        showAlert(Alert.AlertType.INFORMATION, "Book purchased successfully!");
    }

    private void handleRent(Book book, CartItem item) {
        Dialog<String> passDialog = createPasswordDialog(
                "Rent Payment",
                "Enter your password to rent \"" + book.getTitle() + "\""
        );

        Optional<String> passResult = passDialog.showAndWait();
        if (passResult.isEmpty()) return;

        String password = passResult.get();
        if (!userDatabase.validateUser(currentUser, password)) {
            showAlert(Alert.AlertType.ERROR, "Incorrect password. Payment denied.");
            return;
        }

        // Use the pre-selected rental days and due date
        LocalDate dueDate = item.getDueDate();
        int rentalDays = item.getRentalDays();

        if (dueDate == null || rentalDays <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid rental information.");
            return;
        }

        int totalPrice = rentalDays * book.getRentAmount();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Rent");
        confirm.setHeaderText(null);
        confirm.setContentText("Rent for " + rentalDays + " days\nTotal: $" + totalPrice + "\nDue date: " + dueDate + "\n\nProceed?");
        Optional<ButtonType> confirmResult = confirm.showAndWait();
        if (confirmResult.isEmpty() || confirmResult.get() != ButtonType.OK) return;

        // Update book rental
        book.setRentedTo(currentUser);
        book.setAvailable(false);
        book.setDueDate(dueDate.toString());
        bookDatabase.updateBook(book);

        Cart.getInstance().removeItem(item);
        displayCart();

        showAlert(Alert.AlertType.INFORMATION, "Book rented successfully! Due date: " + dueDate);
    }

    private void handlePurchaseAll() {
        var cartItems = Cart.getInstance().getItems();
        if (cartItems.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Your cart is empty!");
            return;
        }

        // Create a copy of the list to avoid concurrent modification issues
        java.util.List<CartItem> itemsCopy = new java.util.ArrayList<>(cartItems);

        // Ask for password once
        Dialog<String> passDialog = createPasswordDialog(
                "Confirm Purchase",
                "Enter your password to confirm purchase of all items"
        );

        Optional<String> passResult = passDialog.showAndWait();
        if (passResult.isEmpty()) return;

        String password = passResult.get();
        if (!userDatabase.validateUser(currentUser, password)) {
            showAlert(Alert.AlertType.ERROR, "Incorrect password. Purchase cancelled.");
            return;
        }

        // Show summary of all items
        StringBuilder summary = new StringBuilder("You are about to purchase:\n\n");
        int totalCost = 0;

        for (CartItem item : itemsCopy) {
            Book book = item.getBook();
            if (item.getAction() == CartItem.ActionType.BUY) {
                summary.append("• BUY: ").append(book.getTitle()).append(" - $").append(book.getBuyAmount()).append("\n");
                totalCost += book.getBuyAmount();
            } else {
                int rentCost = item.getRentalDays() * book.getRentAmount();
                summary.append("• RENT: ").append(book.getTitle())
                        .append(" (").append(item.getRentalDays()).append(" days) - $").append(rentCost).append("\n");
                totalCost += rentCost;
            }
        }

        summary.append("\nTotal: $").append(totalCost);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Purchase All");
        confirm.setHeaderText(null);
        confirm.setContentText(summary.toString());
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        // Process all purchases and rentals
        int processedCount = 0;

        for (CartItem item : itemsCopy) {
            Book book = item.getBook();

            if (item.getAction() == CartItem.ActionType.BUY) {
                String originalOwner = book.getOwner();
                book.setOwner(currentUser);
                book.setAvailable(true);
                bookDatabase.deleteBook(book.getTitle(), originalOwner);
                bookDatabase.addBook(book);
                Cart.getInstance().removeItem(item);
                processedCount++;
            } else {
                LocalDate dueDate = item.getDueDate();
                if (dueDate != null && item.getRentalDays() > 0) {
                    book.setRentedTo(currentUser);
                    book.setAvailable(false);
                    book.setDueDate(dueDate.toString());
                    bookDatabase.updateBook(book);
                    Cart.getInstance().removeItem(item);
                    processedCount++;
                }
            }
        }

        displayCart();

        if (processedCount > 0) {
            showAlert(Alert.AlertType.INFORMATION,
                    "Successfully processed " + processedCount + " item(s)!\nTotal cost: $" + totalCost);
        } else {
            showAlert(Alert.AlertType.WARNING, "No items were processed.");
        }
    }

    private Dialog<String> createPasswordDialog(String title, String header) {
        Dialog<String> passDialog = new Dialog<>();
        passDialog.setTitle(title);
        passDialog.setHeaderText(header);

        PasswordField passwordField = new PasswordField();
        TextField passwordVisibleField = new TextField();
        passwordVisibleField.setPromptText("Password");
        passwordVisibleField.setVisible(false);
        passwordVisibleField.setManaged(false);

        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());

        Button toggleBtn = new Button("👁");
        toggleBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 14px; -fx-text-fill: black;");
        boolean[] isVisible = {false};

        toggleBtn.setOnAction(e -> {
            if (isVisible[0]) { // Hide
                passwordVisibleField.setVisible(false);
                passwordVisibleField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                toggleBtn.setText("👁");
            } else { // Show
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                passwordVisibleField.setVisible(true);
                passwordVisibleField.setManaged(true);
                toggleBtn.setText("👁🗨");
            }
            isVisible[0] = !isVisible[0];
        });

        HBox passwordBox = new HBox(5, passwordField, passwordVisibleField, toggleBtn);
        passwordBox.setStyle("-fx-alignment: CENTER_LEFT;");
        VBox content = new VBox(10, new Label("Password:"), passwordBox);
        content.setMinWidth(350);
        passDialog.getDialogPane().setContent(content);
        passDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        passDialog.setResultConverter(btn -> btn == ButtonType.OK ? passwordField.getText() : null);

        return passDialog;
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
