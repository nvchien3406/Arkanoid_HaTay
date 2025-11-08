module GameController {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires java.sql;


    opens GameController to javafx.fxml;
    exports GameController;
    opens Models to javafx.base;
}