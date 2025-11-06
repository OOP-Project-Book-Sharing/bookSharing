module com.example.first_draft {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens com.example.first_draft to javafx.fxml;
    exports com.example.first_draft;
}
