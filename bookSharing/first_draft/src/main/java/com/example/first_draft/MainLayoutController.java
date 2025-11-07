package com.example.first_draft;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainLayoutController {

    @FXML private Button homeButton;
    @FXML private Button myBooksButton;
    @FXML private Button searchButton;
    @FXML private Button accountButton;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private StackPane mainStackPane;

    private List<Book> books;
    private List<Genre> genres;
    private int currentPageIndex = 0;

    // FXML file list (make sure these files exist under /com/example/first_draft/)
    private final String[] pages = {
            "homeWithGenre.fxml",  // home page with genres
            "myBooks.fxml",       // user's books
            "searchPage.fxml",    // search section
            "accountPage.fxml"    // account page
    };

    public void setBooks(List<Book> books) {
        this.books = books;
        tryLoadFirstPage();
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        tryLoadFirstPage();
    }

    /**
     * Load first page only after both books and genres are set.
     */
    private void tryLoadFirstPage() {
        if (books != null && genres != null && mainStackPane != null && mainStackPane.getChildren().isEmpty()) {
            loadPage(pages[currentPageIndex]);
        }
    }

    @FXML
    private void initialize() {
        homeButton.setOnAction(e -> switchTo(0));
        myBooksButton.setOnAction(e -> switchTo(1));
        searchButton.setOnAction(e -> switchTo(2));
        accountButton.setOnAction(e -> switchTo(3));

        prevButton.setOnAction(e -> switchTo((currentPageIndex - 1 + pages.length) % pages.length));
        nextButton.setOnAction(e -> switchTo((currentPageIndex + 1) % pages.length));
    }

    private void switchTo(int index) {
        currentPageIndex = index;
        loadPage(pages[index]);
    }

    private void loadPage(String fxmlFile) {
        try {
            URL fxmlUrl = getClass().getResource("/com/example/first_draft/" + fxmlFile);
            if (fxmlUrl == null) {
                System.err.println("❌ FXML not found: " + fxmlFile);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Node page = loader.load();

            // Pass data to specific controllers if needed
            Object controller = loader.getController();

            if (controller instanceof myBooksController myBooksCtrl) {
                myBooksCtrl.setBooks(books);
                myBooksCtrl.displayBooks();
            } else if (controller instanceof GenreController genreCtrl) {
                genreCtrl.setGenres(genres);
                genreCtrl.loadGenres();
            } else if (controller instanceof SearchPageController srchCtrl) {
                srchCtrl.setBooks(books);
                srchCtrl.displayBooks();
            }

            mainStackPane.getChildren().setAll(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
