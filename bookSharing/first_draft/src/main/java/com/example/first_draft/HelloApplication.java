package com.example.first_draft;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("myBooks.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);

        myBooksController controller = fxmlLoader.getController();

        Book book1 = new Book("Death on the Nile", "Agatha Christie", "Fantasy adventure story.",
                "/com/example/first_draft/images/book1.jpeg", -1, 150, true);
        Book book2 = new Book("The Murder of Roger Ackroyd", "Agatha Christie",
                "A classic detective novel.",
                "/com/example/first_draft/images/roger_ackroyd.jpg", 250, -1, true);
        Book book3 = new Book("Byokmesh", "Agatha Christie",
                "A classic detective novel.",
                "/com/example/first_draft/images/byokmesh.jpg", 250, 100, true);
        Book book4 = new Book("Opshora theatre er mamla", "Agatha Christie",
                "A classic detective novel.",
                "/com/example/first_draft/images/feluda.jpg", -1, 100, true);
        Book book5 = new Book("And there were none", "Agatha Christie",
                "A classic detective novel.",
                "/com/example/first_draft/images/poirot.jpg", 250, 100, true);

        controller.setBooks(Arrays.asList(book1, book2, book3, book4, book5));
        controller.displayBooks();

        stage.setTitle("Book Rental Home");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
