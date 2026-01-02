module com.example.first_draft {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
   // requires com.google.gson;

    opens com.example.first_draft to javafx.fxml;
    exports com.example.first_draft;
    exports com.example.first_draft.cart;
    opens com.example.first_draft.cart to javafx.fxml;
    exports com.example.first_draft.chat;
    opens com.example.first_draft.chat to javafx.fxml;
    exports com.example.first_draft.controller;
    opens com.example.first_draft.controller to javafx.fxml;
}
