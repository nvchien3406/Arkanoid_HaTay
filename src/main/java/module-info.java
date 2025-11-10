module GameController {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires java.sql;
    requires javafx.media;
    requires java.prefs;


    opens GameController to javafx.fxml;
    exports GameController;
    opens Models to javafx.base;
}