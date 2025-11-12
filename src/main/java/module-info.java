module GameController {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires java.desktop;
    requires java.sql;
    requires java.prefs;
//    requires GameController;

    exports GameController;
    opens GameController to javafx.fxml;

//    exports Sounds; // <— CHỈ cần dòng này
//    opens Sounds to javafx.fxml;

    exports DAO;
    opens DAO to javafx.fxml;

    //opens Models to javafx.base;
    opens Models.Ball to javafx.base;
    opens Models.Brick to javafx.base;
    opens Models.PowerUp_Down to javafx.base;
    opens Models.PowerUpFactoryMethod to javafx.base;
    opens Models.Paddle to javafx.base;
    opens Models.Object to javafx.base;
    opens Models.Player to javafx.base;
    opens Models.Interface to javafx.base;
    exports GameController.Manager;
    opens GameController.Manager to javafx.fxml;
    exports GameController.Controllers;
    opens GameController.Controllers to javafx.fxml;
    exports GameController.GameConstants;
    opens GameController.GameConstants to javafx.fxml;
    opens Models.Level to javafx.base;
}
