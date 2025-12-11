package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class MyBooksController {

    @FXML private Button btnForSaleRent;
    @FXML private Button btnRented;
    @FXML private Button btnBorrowed;
    @FXML private Button btnAddBook;
    @FXML private ScrollPane scrollPane;
    @FXML private VBox bookListVBox;

    private String username;
    private BookDatabase bookDatabase = new BookDatabase();
    private List<Book> allBooks;

    private enum ViewMode { FOR_SALE_RENT, BORROWED, RENTED_OUT }
    private ViewMode currentMode = ViewMode.FOR_SALE_RENT;

    public void setUsername(String user) {
        this.username = user;
        allBooks = bookDatabase.getAllBooks();
        refresh();
    }

    @FXML
    public void initialize() {
        btnForSaleRent.setOnAction(e -> {
            currentMode = ViewMode.FOR_SALE_RENT;
            allBooks = bookDatabase.getAllBooks();
            showForSaleRent();
        });
        btnBorrowed.setOnAction(e -> {
            currentMode = ViewMode.BORROWED;
            allBooks = bookDatabase.getAllBooks();
            showBorrowed();
        });
        btnRented.setOnAction(e -> {
            currentMode = ViewMode.RENTED_OUT;
            allBooks = bookDatabase.getAllBooks();
            showRentedOut();
        });
        btnAddBook.setOnAction(e -> openAddBookPage());
    }

    public void refresh() {
        allBooks = bookDatabase.getAllBooks();
        showForSaleRent();
    }

    private void showForSaleRent() {
        displayFiltered(book ->
                book.getOwner() != null &&
                        book.getOwner().equalsIgnoreCase(username) &&
                        (book.getRentedTo() == null || book.getRentedTo().isEmpty())
        );
    }

    private void showBorrowed() {
        displayFiltered(book ->
                username.equalsIgnoreCase(book.getRentedTo()) &&
                        !username.equalsIgnoreCase(book.getOwner())
        );
    }

    private void showRentedOut() {
        displayFiltered(book ->
                book.getOwner() != null &&
                        book.getOwner().equalsIgnoreCase(username) &&
                        book.getRentedTo() != null &&
                        !book.getRentedTo().isEmpty()
        );
    }

    private interface Filter { boolean test(Book b); }

    private void displayFiltered(Filter filter) {
        bookListVBox.getChildren().clear();

        boolean foundAny = false;

        for (Book book : allBooks) {
            if (!filter.test(book)) continue;
            foundAny = true;

            HBox bookRow = new HBox(20);
            bookRow.getStyleClass().add("book-row");
            bookRow.setPadding(new Insets(20));
            bookRow.setAlignment(Pos.TOP_LEFT);

            GridPane infoGrid = new GridPane();
            infoGrid.setHgap(20);
            infoGrid.setVgap(8);
            infoGrid.getStyleClass().add("details-grid");

            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(50);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(30);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(20);
            infoGrid.getColumnConstraints().addAll(col1, col2, col3);

            Label titleLabel = new Label("Title: " + book.getTitle());
            titleLabel.getStyleClass().add("book-title");

            Label authorLabel = new Label("Author: " + book.getAuthor());
            authorLabel.getStyleClass().add("book-author");

            Label genreLabel = new Label("Genre: " + book.getGenre());
            genreLabel.getStyleClass().add("book-genre");

            Label ownerLabel = new Label("Owner: " + book.getOwner());
            ownerLabel.getStyleClass().add("book-owner");

            Label rentedToLabel = new Label(
                    (book.getRentedTo() == null || book.getRentedTo().isEmpty())
                            ? "Rented To: —"
                            : "Rented To: " + book.getRentedTo()
            );
            rentedToLabel.getStyleClass().add("book-rented");

            Label dueDateLabel = new Label(
                    (book.getDueDate() == null || book.getDueDate().isEmpty())
                            ? "Due Date: —"
                            : "Due Date: " + book.getDueDate()
            );
            dueDateLabel.getStyleClass().add("book-due");
            Label buyAmountLabel;
            if(book.getBuyAmount()>0) {
                buyAmountLabel = new Label("Buy Price: " + book.getBuyAmount());
                buyAmountLabel.getStyleClass().add("book-buy");
            }
            else {
                buyAmountLabel = new Label("Buy Price: -");
                buyAmountLabel.getStyleClass().add("book-buy");
            }
            Label rentAmountLabel;
            if(book.getRentAmount()>0) {
                rentAmountLabel = new Label("Rent Price: " + book.getRentAmount());
                rentAmountLabel.getStyleClass().add("book-buy");
            }
            else {
                rentAmountLabel = new Label("Rent Price: -");
                rentAmountLabel.getStyleClass().add("book-buy");
            }

            infoGrid.add(titleLabel, 0, 0);
            infoGrid.add(authorLabel, 0, 1);
            infoGrid.add(genreLabel, 0, 2);
            infoGrid.add(ownerLabel, 0, 3);

            infoGrid.add(rentedToLabel, 1, 1);
            infoGrid.add(rentAmountLabel, 1, 2);
            infoGrid.add(dueDateLabel, 1, 3);
            infoGrid.add(buyAmountLabel, 1, 0);



            if (currentMode == ViewMode.FOR_SALE_RENT) {
                Button editButton = new Button("Edit Details");
                editButton.getStyleClass().add("edit-button");
                editButton.setOnAction(e -> openEditBook(book));
                infoGrid.add(editButton, 0, 6, 2, 1);

                Button deleteButton = new Button("Delete");
                deleteButton.getStyleClass().add("edit-button");

                deleteButton.setOnAction(e -> {
                    if (showConfirmDialog("Are you sure you want to delete this book?")) {
                        bookDatabase.deleteBook(book.getTitle());
                        allBooks = bookDatabase.getAllBooks();
                        refresh();
                    }
                });

                infoGrid.add(deleteButton, 1, 6, 1, 1);
            }

            else if (currentMode == ViewMode.RENTED_OUT) {
                Label info = new Label("Currently rented out.");
                infoGrid.add(info, 0, 6, 2, 1);
            }

            else if (currentMode == ViewMode.BORROWED) {
                Button returnButton = new Button("Return Book");
                returnButton.getStyleClass().add("edit-button");
                returnButton.setOnAction(e -> handleReturnBook(book));
                infoGrid.add(returnButton, 0, 6, 2, 1);
            }

            ImageView cover = book.getCover();
            cover.getStyleClass().add("book-cover");
            cover.setFitWidth(140);
            cover.setFitHeight(180);
            cover.setPreserveRatio(true);

            HBox.setHgrow(infoGrid, Priority.ALWAYS);
            infoGrid.maxWidthProperty().bind(bookListVBox.widthProperty().subtract(190));

            bookRow.getChildren().addAll(infoGrid, cover);
            bookListVBox.getChildren().add(bookRow);
        }

        scrollPane.setFitToWidth(true);

        if (!foundAny) {
            Label emptyMsg = new Label();
            emptyMsg.setStyle("-fx-font-size: 20px; -fx-text-fill: grey;");

            switch (currentMode) {
                case FOR_SALE_RENT -> emptyMsg.setText("NO BOOKS FOR SALE/RENT");
                case BORROWED -> emptyMsg.setText("NO BOOKS BORROWED");
                case RENTED_OUT -> emptyMsg.setText("NO BOOKS THAT YOU RENTED OUT");
            }

            bookListVBox.getChildren().add(emptyMsg);
        }
    }

    private boolean showConfirmDialog(String message) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Confirm");

        VBox box = new VBox(15);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);

        Label label = new Label(message);

        Button yes = new Button("Yes");
        Button no = new Button("No");

        HBox buttons = new HBox(10, yes, no);
        buttons.setAlignment(Pos.CENTER);

        box.getChildren().addAll(label, buttons);

        final boolean[] result = {false};

        yes.setOnAction(e -> {
            result[0] = true;
            dialog.close();
        });

        no.setOnAction(e -> dialog.close());

        dialog.setScene(new Scene(box));
        dialog.setResizable(false);
        dialog.showAndWait();

        return result[0];
    }

    private void handleReturnBook(Book book) {
        try {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Enter Password");

            VBox box = new VBox(15);
            box.setPadding(new Insets(20));
            box.setAlignment(Pos.CENTER);

            Label label = new Label("Enter your password to confirm return:");
            PasswordField passField = new PasswordField();

            Button confirm = new Button("Confirm");
            Button cancel = new Button("Cancel");

            HBox buttons = new HBox(10, confirm, cancel);
            buttons.setAlignment(Pos.CENTER);

            box.getChildren().addAll(label, passField, buttons);

            confirm.setOnAction(ev -> {
                String password = passField.getText().trim();

                UserDatabase userDb = new UserDatabase();
                if (!userDb.validateUser(username, password)) {
                    label.setText("Wrong password! Try again.");
                    return;
                }

                book.setAvailable(true);
                book.setRentedTo(null);
                book.setDueDate(null);

                bookDatabase.updateBook(book);

                dialog.close();

                allBooks = bookDatabase.getAllBooks();
                showBorrowed();
            });

            cancel.setOnAction(ev -> dialog.close());

            dialog.setScene(new Scene(box));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAddBookPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/addBook.fxml"));
            Parent root = loader.load();

            AddBookController ctrl = loader.getController();
            ctrl.setBookDatabase(this.bookDatabase);
            ctrl.setUsername(this.username);

            Stage popup = new Stage();
            popup.setTitle("Add New Book");
            popup.setScene(new Scene(root));
            popup.initOwner(SceneManager.getMainStackPane().getScene().getWindow());
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setResizable(false);

            popup.showAndWait();
            allBooks = bookDatabase.getAllBooks();
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openEditBook(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/addBook.fxml"));
            Parent root = loader.load();

            AddBookController ctrl = loader.getController();
            ctrl.setBookDatabase(this.bookDatabase);
            ctrl.setUsername(this.username);
            ctrl.setBookToEdit(book);

            Stage popup = new Stage();
            popup.setTitle("Edit Book");
            popup.setScene(new Scene(root));
            popup.initOwner(SceneManager.getMainStackPane().getScene().getWindow());
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setResizable(false);

            popup.showAndWait();
            allBooks = bookDatabase.getAllBooks();
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
