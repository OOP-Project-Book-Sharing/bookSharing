package com.example.first_draft;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.first_draft.Cart;
import com.example.first_draft.CartItem;
import com.example.first_draft.CartController;

public class SearchPageController {

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button viewCartButton;

    private List<Book> books;
    private String username;

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void displayBooks() {
        renderBookGrid(this.books);
    }

    @FXML
    public void search(ActionEvent e) {
        if (searchTextField.getText().isEmpty()) {
            displayBooks();
            return;
        }
        ArrayList<Book> searchResults = searchlist(searchTextField.getText());
        renderBookGrid(searchResults);
    }

    // --- Helper method to fix the layout issue ---
    private void renderBookGrid(List<Book> booksToDisplay) {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();
        gridPane.getRowConstraints().clear();

        // Center the grid so there isn't awkward empty space on the right
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));

        if (booksToDisplay == null || booksToDisplay.isEmpty()) {
            // Show "No matches found" message
            Label noResultsLabel = new Label("No matches found");
            noResultsLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666; -fx-font-weight: bold;");
            gridPane.add(noResultsLabel, 0, 0);
            return;
        }

        int columns = 4;
        int col = 0, row = 0;


        for (Book book : booksToDisplay) {
            Label title = new Label(book.getTitle());
            title.setStyle("-fx-font-weight: bold;");

            Label author = new Label(book.getAuthor());

            ImageView cover = book.getCover();
            cover.setFitWidth(120);
            cover.setFitHeight(160);
            cover.setPreserveRatio(true);

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

            Button detailsButton = new Button("See Details");
            detailsButton.setStyle("-fx-font-size: 10px; -fx-padding: 4 8 4 8;");
            detailsButton.setOnAction(event -> openBookDetails(book));

            // Add to Cart button
            Button addToCart = new Button("Add to Cart");
            addToCart.setStyle("-fx-font-size: 10px; -fx-padding: 4 8 4 8;");
            addToCart.setOnAction(event -> {
                // Can't add own books or unavailable books
                if (username != null && book.getOwner().equalsIgnoreCase(username)) {
                    showAlert(Alert.AlertType.ERROR, "You cannot add your own book to the cart.");
                    return;
                }
                if (!book.isAvailable() || (book.getRentedTo() != null && !book.getRentedTo().isEmpty())) {
                    showAlert(Alert.AlertType.ERROR, "Book is not available.");
                    return;
                }

                List<String> choices = new ArrayList<>();
                if (book.getBuyAmount() > 0) choices.add("Buy");
                if (book.getRentAmount() > 0) choices.add("Rent");

                if (choices.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "This book cannot be bought or rented.");
                    return;
                }

                ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
                dialog.setTitle("Add to Cart");
                dialog.setHeaderText("Choose action for \"" + book.getTitle() + "\"");
                Optional<String> result = dialog.showAndWait();
                if (result.isEmpty()) return;
                String action = result.get();

                CartItem.ActionType type = action.equals("Buy") ? CartItem.ActionType.BUY : CartItem.ActionType.RENT;

                // If renting, ask for rental duration
                if (type == CartItem.ActionType.RENT) {
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

                    CartItem item = new CartItem(book, type, (int) days, dueDate);
                    Cart.getInstance().addItem(item);
                    showAlert(Alert.AlertType.INFORMATION,
                        "Added to cart: " + book.getTitle() + " (Rent for " + days + " days - $" + totalCost + ")");
                } else {
                    // For buy, no date needed
                    CartItem item = new CartItem(book, type);
                    Cart.getInstance().addItem(item);
                    showAlert(Alert.AlertType.INFORMATION, "Added to cart: " + book.getTitle() + " (Buy - $" + book.getBuyAmount() + ")");
                }
            });

            VBox vbox = new VBox(8);
            vbox.setAlignment(Pos.TOP_CENTER);
            vbox.setPadding(new Insets(10));
            vbox.setPrefWidth(160);
            vbox.setPrefHeight(270);
            vbox.getChildren().addAll(cover, title, author, statusLabel, detailsButton, addToCart);

            vbox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; "
                    + "-fx-border-radius: 6; -fx-background-radius: 6;");
            vbox.getStyleClass().add("book-card");

            // Add click event to entire card
            vbox.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    openBookDetails(book);
                }
            });
            vbox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; "
                    + "-fx-border-radius: 6; -fx-background-radius: 6; -fx-cursor: hand;");

            gridPane.add(vbox, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }
    }

    public ArrayList<Book> searchlist(String searchKeyword) {
        ArrayList<Book> searchResults = new ArrayList<>();
        if (books == null) return searchResults;

        for (Book book : books) {
            if (username != null && book.getOwner().equalsIgnoreCase(username)) continue;
            if (!book.isAvailable()) continue;
            if (book.getRentedTo() != null && !book.getRentedTo().isEmpty()) continue;

            // Search by title, author, or owner username
            if (book.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                    book.getAuthor().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                    book.getOwner().toLowerCase().contains(searchKeyword.toLowerCase())) {
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

    @FXML
    private void openCart(ActionEvent e) {
        SceneManager.switchViewWithData("/com/example/first_draft/cart.fxml", controller -> {
            if (controller instanceof CartController c) {
                c.setCurrentUser(username);
                c.displayCart();
            }
        });
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public List<Book> getBooks() {
        return books;
    }
}