package GameController;

import Models.Brick;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("startGame.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        // Lấy controller sau khi load FXML
        StartGameController controller = fxmlLoader.getController();

        // Tạo danh sách bricks rồi truyền vào LoadBrick()
        GameManager gameManager = GameManager.getInstance();
        gameManager.startGame(controller);

        stage.setTitle("Arkanoid Game");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}