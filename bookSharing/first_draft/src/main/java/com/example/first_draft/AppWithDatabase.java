package com.example.first_draft;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AppWithDatabase extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homeWithGenre.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        List<Book>books = DatabaseManager.loadBooks("Detective.txt");
        List<Book>books1 = DatabaseManager.loadBooks("Thriller.txt");
        //HomePageController controller = fxmlLoader.getController();
        GenreController genreController = fxmlLoader.getController();

//        Book book1 = new Book("Death on the Nile", "Agatha Christie", "Fantasy adventure story.",
//                "/com/example/first_draft/images/book1.jpg", 300, 150, true);
//        Book book2 = new Book("The Murder of Roger Ackroyd", "Agatha Christie",
//                "A classic detective novel.",
//                "/com/example/first_draft/images/roger_ackroyd.jpg", 250, 100, true);
//        Book book3 = new Book("Feluda", "Styajit Ray", "Fantasy adventure story.",
//                "/com/example/first_draft/images/feluda.jpg", 300, 150, true);
//        Book book4 = new Book("Byomkesh", "Swardindu",
//                "A classic detective novel.",
//                "/com/example/first_draft/images/byomkesh.jpg", 250, 100, true);
//


//        books.add(book1);
//        books.add(book2);
//
//        books1.add(book3);
//        books1.add(book4);

        Genre genre1 = new Genre("Detective",books);
        Genre genre2 = new Genre("Thriller",books1);

        genreController.setGenres(Arrays.asList(genre1,genre2));

        stage.setTitle("Book Rental Home");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
