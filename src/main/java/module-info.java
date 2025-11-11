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
    opens Models.Ball to javafx.base;
    opens Models.Brick to javafx.base;
    opens Models.PowerUp_Down to javafx.base;
    opens Models.PowerUpFactoryMethod to javafx.base;
    opens Models.Paddle to javafx.base;
    opens Models.Object to javafx.base;
    opens Models.Player to javafx.base;
    opens Models.Interface to javafx.base;
    exports DAO;
    opens DAO to javafx.fxml;
}