package com.example.first_draft;

import javafx.fxml.FXML;
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
    private UserDatabase userDatabase = new UserDatabase();

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
        // Password dialog
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

        // ---- DELETE BOOK ----
        book.setOwner(currentUser);
        book.setAvailable(true);
        bookDatabase.updateBook(book);
        //bookDatabase.deleteBook(book.getTitle());

        redirectToSearchPage();

        showAlert(Alert.AlertType.INFORMATION, "Book purchased successfully!");
    }

    private void handleRent() {
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

        // Due date selection
        Dialog<LocalDate> dateDialog = new Dialog<>();
        dateDialog.setTitle("Select Due Date");
        dateDialog.setHeaderText("Choose a due date for returning the book");

        DatePicker datePicker = new DatePicker(LocalDate.now().plusDays(1));
        VBox dateContent = new VBox(10, new Label("Due Date:"), datePicker);
        dateContent.setMinWidth(300);
        dateDialog.getDialogPane().setContent(dateContent);
        dateDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dateDialog.setResultConverter(btn -> btn == ButtonType.OK ? datePicker.getValue() : null);

        Optional<LocalDate> dateResult = dateDialog.showAndWait();
        if (dateResult.isEmpty()) return;

        LocalDate dueDate = dateResult.get();
        long days = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);

        if (days <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid due date selected.");
            return;
        }

        int totalPrice = (int) (days * book.getRentAmount());

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Rent");
        confirm.setHeaderText(null);
        confirm.setContentText("Total rent: $" + totalPrice + "\nProceed to rent?");
        Optional<ButtonType> confirmResult = confirm.showAndWait();
        if (confirmResult.isEmpty() || confirmResult.get() != ButtonType.OK) return;

        // Update book rental
        book.setRentedTo(currentUser);
        book.setAvailable(false);
        book.setDueDate(dueDate.toString());
        bookDatabase.updateBook(book);

        redirectToSearchPage();

        showAlert(Alert.AlertType.INFORMATION, "Book rented successfully! Due date: " + dueDate);
    }

    private Dialog<String> createPasswordDialog(String title, String header) {
        Dialog<String> passDialog = new Dialog<>();
        passDialog.setTitle(title);
        passDialog.setHeaderText(header);

        PasswordField passwordField = new PasswordField();
        VBox content = new VBox(10, new Label("Password:"), passwordField);
        content.setMinWidth(300);
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

    private void redirectToSearchPage() {
        SceneManager.switchViewWithData("/com/example/first_draft/searchPage.fxml", controller -> {
            BookDatabase db = new BookDatabase();
            if (controller instanceof SearchPageController c) {
                c.setBooks(db.getAvailableBooksNotOwnedBy(currentUser));
                c.setUsername(currentUser);
                c.displayBooks();
            }
        });
    }
}
