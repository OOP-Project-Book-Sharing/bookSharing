package com.example.first_draft;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class demo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // ✅ Load main layout (use absolute path)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/first_draft/mainLayout.fxml"));
        Scene scene = new Scene(loader.load(), 600, 500);

        // ✅ Create book lists
        List<Book> booksDetective = new ArrayList<>();
        List<Book> booksThriller = new ArrayList<>();

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

        booksDetective.addAll(Arrays.asList(book1, book2));
        booksThriller.addAll(Arrays.asList(book3, book4));

        // ✅ Create genres
        Genre genre1 = new Genre("Detective", booksDetective);
        Genre genre2 = new Genre("Thriller", booksThriller);

        // ✅ Get main layout controller
        MainLayoutController controller = loader.getController();

        // ✅ Pass both lists to controller (order doesn’t matter now)
        controller.setBooks(Arrays.asList(book1, book2, book3, book4));
        controller.setGenres(Arrays.asList(genre1, genre2));

        stage.setTitle("📚 Book Sharing App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
