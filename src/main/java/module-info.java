module GameController {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens GameController to javafx.fxml;
    exports GameController;
}