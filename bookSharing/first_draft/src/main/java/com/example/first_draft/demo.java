//package com.example.first_draft;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import java.util.Arrays;
//import java.util.List;
//
//public class demo extends Application {
//    @Override
//    public void start(Stage stage) throws Exception {
//        // Load main layout instead of a single page
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainLayout.fxml"));
//        Scene scene = new Scene(loader.load(), 600, 500);
//
//        // Get controller of MainLayout
//        MainLayoutController controller = loader.getController();
//
//        // Prepare books
//        List<Book> books = Arrays.asList(
//                new Book("Death on the Nile", "Agatha Christie", "Fantasy adventure story.",
//                        "/com/example/first_draft/images/book1.jpeg", -1, 150, true),
//                new Book("The Murder of Roger Ackroyd", "Agatha Christie", "A classic detective novel.",
//                        "/com/example/first_draft/images/roger_ackroyd.jpg", 250, -1, true),
//                new Book("Byokmesh", "Agatha Christie", "A classic detective novel.",
//                        "/com/example/first_draft/images/byokmesh.jpg", 250, 100, true),
//                new Book("Opshora theatre er mamla", "Agatha Christie", "A classic detective novel.",
//                        "/com/example/first_draft/images/feluda.jpg", -1, 100, true),
//                new Book("And there were none", "Agatha Christie", "A classic detective novel.",
//                        "/com/example/first_draft/images/poirot.jpg", 250, 100, true)
//        );
//
//        // Pass books to main controller
//
//        stage.setTitle("Book Sharing App");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}
