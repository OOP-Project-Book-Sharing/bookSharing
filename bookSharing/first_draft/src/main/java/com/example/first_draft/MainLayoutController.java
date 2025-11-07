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

    private List<Book> books;
    private List<Genre> genres;

    @FXML
    public void initialize() {
        // Register the StackPane globally for SceneManager
        SceneManager.setMainStackPane(mainStackPane);

        // Button actions to switch pages
        //prevButton.setOnAction(e -> SceneManager.goBack());
        homeButton.setOnAction(e -> loadPage("homeWithGenre.fxml"));
        myBooksButton.setOnAction(e -> loadPage("myBooks.fxml"));
        searchButton.setOnAction(e -> loadPage("homePage.fxml"));
        accountButton.setOnAction(e -> loadPage("accountPage.fxml"));

        // Do NOT load a page here; wait until books and genres are set
    }

    /** Called from LoginController to inject books */
    public void setBooks(List<Book> books) {
        this.books = books;
        tryLoadFirstPage();
    }

    /** Called from LoginController to inject genres */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
        tryLoadFirstPage();
    }

    /** Load the first page only after books & genres are available */
    private void tryLoadFirstPage() {
        if (books != null && genres != null && mainStackPane != null && mainStackPane.getChildren().isEmpty()) {
            loadPage("homeWithGenre.fxml");
        }
    }

    /** Load an FXML page and inject data if required */
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

            if (page instanceof Region region) {
                region.prefWidthProperty().bind(mainStackPane.widthProperty());
                region.prefHeightProperty().bind(mainStackPane.heightProperty());
            }

            // Inject books/genres into specific controllers
            if (controller instanceof myBooksController myBooksCtrl) {
                myBooksCtrl.setBooks(books);
                myBooksCtrl.displayBooks();
            } else if (controller instanceof GenreController genreCtrl) {
                genreCtrl.setGenres(genres);
                genreCtrl.loadGenres();
            } else if (controller instanceof HomePageController homeCtrl) {
                homeCtrl.setBooks(books);
                homeCtrl.displayBooks();
            }

            // Replace current page (do NOT stack)
            mainStackPane.getChildren().setAll(page);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
