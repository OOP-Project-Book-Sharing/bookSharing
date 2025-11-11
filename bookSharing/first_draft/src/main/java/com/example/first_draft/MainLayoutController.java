package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainLayoutController {

    @FXML private StackPane mainStackPane;
    @FXML private Button homeButton;
    @FXML private Button myBooksButton;
    @FXML private Button searchButton;
    @FXML private Button accountButton;
    @FXML private Button prevButton;

    private BookDatabase bookDatabase;
    private String currentUser;

    /** Inject BookDatabase */
    public void setBookDatabase(BookDatabase bookDatabase) {
        this.bookDatabase = bookDatabase;
        tryLoadFirstPage();
    }

    /** Inject current logged-in user */
    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
        tryLoadFirstPage();
    }

    @FXML
    public void initialize() {
        // Register the StackPane globally for SceneManager
        SceneManager.setMainStackPane(mainStackPane);

        // Button actions to switch pages
        homeButton.setOnAction(e -> loadPage("homeWithGenre.fxml"));
        myBooksButton.setOnAction(e -> loadPage("myBooks.fxml"));
        searchButton.setOnAction(e -> loadPage("homePage.fxml"));
        accountButton.setOnAction(e -> loadPage("accountPage.fxml"));
    }

    /** Load the first page only after DB and user are available */
    private void tryLoadFirstPage() {
        if (bookDatabase != null && currentUser != null && mainStackPane != null && mainStackPane.getChildren().isEmpty()) {
            loadPage("homeWithGenre.fxml");
        }
    }

    /** Load an FXML page and inject filtered books if needed */
    private void loadPage(String fxmlFile) {
        try {
            URL fxmlUrl = getClass().getResource("/com/example/first_draft/" + fxmlFile);
            if (fxmlUrl == null) {
                System.err.println("❌ FXML not found: " + fxmlFile);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Node page = loader.load();
            Object controller = loader.getController();

            // --- Pass filtered book lists to controllers ---
            if (controller instanceof myBooksController myBooksCtrl) {
                // Books owned by current user
                List<Book> myBooks = bookDatabase.getBooksByOwner(currentUser);
                myBooksCtrl.displayBooks();

            } else if (controller instanceof GenreController genreCtrl) {
                // Pass all books so controller can filter per genre dynamically
                genreCtrl.setBooks(bookDatabase.getAllBooks());

            } else if (controller instanceof HomePageController homeCtrl) {
                // Show all available books
                List<Book> availableBooks = bookDatabase.getAvailableBooks();
                homeCtrl.setBooks(availableBooks);
                homeCtrl.displayBooks();

            } else if (controller instanceof HomePageController searchCtrl) {
                // Show all books, can be filtered later
                searchCtrl.setBooks(bookDatabase.getAllBooks());
                searchCtrl.displayBooks();
            }

            if (page instanceof Region region) {
                region.prefWidthProperty().bind(mainStackPane.widthProperty());
                region.prefHeightProperty().bind(mainStackPane.heightProperty());
            }

            // Replace current page
            mainStackPane.getChildren().setAll(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Utility: Get books rented to current user */
    public List<Book> getRentedBooksForUser() {
        return bookDatabase.getBooksRentedTo(currentUser);
    }
}
