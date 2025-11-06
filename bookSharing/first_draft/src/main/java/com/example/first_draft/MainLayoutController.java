//package com.example.first_draft;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.control.Button;
//import javafx.scene.layout.StackPane;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.List;
//
//public class MainLayoutController {
//
//    @FXML private Button homeButton;
//    @FXML private Button myBooksButton;
//    @FXML private Button searchButton;
//    @FXML private Button accountButton;
//    @FXML private Button prevButton;
//    @FXML private Button nextButton;
//    @FXML private StackPane mainStackPane;
//
//    private List<Book> books;
//    private int currentPageIndex = 0;
//
//    // FXML file list
//    private final String[] pages = {
//            "homeWthGenre.fxml",
//            "myBooks.fxml",
//            "homePage.fxml",
//            "accountPage.fxml"
//    };
//
//    public void setBooks(List<Book> books) {
//        this.books = books;
//        // Load first page when books are available
//        loadPage(pages[currentPageIndex]);
//    }
//
//    @FXML
//    private void initialize() {
//        homeButton.setOnAction(e -> switchTo(0));
//        myBooksButton.setOnAction(e -> switchTo(1));
//        searchButton.setOnAction(e -> switchTo(2));
//        accountButton.setOnAction(e -> switchTo(3));
//
//        prevButton.setOnAction(e -> switchTo((currentPageIndex - 1 + pages.length) % pages.length));
//        nextButton.setOnAction(e -> switchTo((currentPageIndex + 1) % pages.length));
//    }
//
//    private void switchTo(int index) {
//        currentPageIndex = index;
//        loadPage(pages[index]);
//    }
//
//    private void loadPage(String fxmlFile) {
//        try {
//            URL fxmlUrl = getClass().getResource("/com/example/first_draft/" + fxmlFile);
//            if (fxmlUrl == null) {
//                System.err.println("FXML not found: " + fxmlFile);
//                return;
//            }
//
//            FXMLLoader loader = new FXMLLoader(fxmlUrl);
//            Node page = loader.load();
//
//            // Pass books if applicable
//            Object controller = loader.getController();
//            if (controller instanceof myBooksController myBooksCtrl) {
//                myBooksCtrl.setBooks(books);
//                myBooksCtrl.displayBooks();
//            }
//            // You can extend this for other pages too:
//            else if (controller instanceof GenreController homeCtrl) {
//                homeCtrl.setGenres(books);
//            } else if (controller instanceof HomePageController searchCtrl) {
//                searchCtrl.setBooks(books);
//            }
//
//            mainStackPane.getChildren().setAll(page);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
