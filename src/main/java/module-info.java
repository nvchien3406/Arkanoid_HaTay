module GameController {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires java.sql;
    requires javafx.media;
    requires java.prefs;
    //requires GameController;


    opens GameController to javafx.fxml;
    exports GameController;
    opens Models to javafx.base;
    exports DAO;
    opens DAO to javafx.fxml;
}