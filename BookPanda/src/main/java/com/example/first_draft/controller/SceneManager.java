package com.example.first_draft.controller;

import com.example.first_draft.Book;
import com.example.first_draft.BookDatabase;
import com.example.first_draft.cart.CartController;
import com.example.first_draft.chat.ChatPageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public class SceneManager {

    private static StackPane mainStackPane; // static to stack pane on the same stackPane
    private static String currentUser;

    private static class SceneEntry {
        String fxmlPath;
        Consumer<Object> controllerInitializer;

        SceneEntry(String fxmlPath, Consumer<Object> controllerInitializer) {
            this.fxmlPath = fxmlPath;
            this.controllerInitializer = controllerInitializer;
        }
    }

    private static Stack<SceneEntry> history = new Stack<>();
    private static String currentFXML = null;
    private static Consumer<Object> currentInitializer = null;

    public static void setMainStackPane(StackPane pane) {
        mainStackPane = pane;
    }

    public static StackPane getMainStackPane() {
        return mainStackPane;
    }

    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    public static void switchView(String fxmlPath) {
        switchViewWithData(fxmlPath, null);
    }

    public static void switchViewWithData(String fxmlPath, Consumer<Object> controllerInitializer) {
        try {
            if (currentFXML != null && !currentFXML.equals(fxmlPath)) {
                history.push(new SceneEntry(currentFXML, currentInitializer));
            }

            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();

            if (controllerInitializer != null) {
                controllerInitializer.accept(controller);
            }

            // Existing logic for injecting data into controllers
            BookDatabase bookDatabase = new BookDatabase();
            List<Book> allBooks = bookDatabase.getAvailableBooksNotOwnedBy(currentUser);

            if (controller instanceof MyBooksController ctrl) {
                ctrl.setUsername(currentUser);
            } else if (controller instanceof HomePageController homeCtrl) {
                homeCtrl.setCurrentUser(currentUser);
                homeCtrl.setBooks(allBooks);
                homeCtrl.displayBooks();
            } else if (controller instanceof SearchPageController searchCtrl) {
                searchCtrl.setUsername(currentUser);
                if (searchCtrl.getBooks() == null) {
                    searchCtrl.setBooks(allBooks);
                }
                searchCtrl.displayBooks();
            } else if (controller instanceof ChatPageController chatCtrl) {
                chatCtrl.setUsername(currentUser);
            } else if (controller instanceof AddBookController addCtrl) {
                addCtrl.setUsername(currentUser);
                addCtrl.setBookDatabase(bookDatabase);
            } else if (controller instanceof AccountPageController accCtrl) {
                accCtrl.setUsername(currentUser);
            } else if (controller instanceof CartController cartCtrl) {
                cartCtrl.setCurrentUser(currentUser);
                cartCtrl.displayCart();
            }

            mainStackPane.getChildren().setAll(root);

            // Update current scene info
            currentFXML = fxmlPath;
            currentInitializer = controllerInitializer;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goBack() {
        if (!history.isEmpty()) {
            SceneEntry prev = history.pop();
            switchViewWithData(prev.fxmlPath, prev.controllerInitializer);
        }
    }

    public static void clearHistory() {
        history.clear();
    }
}
