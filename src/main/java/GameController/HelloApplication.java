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
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());

        // Lấy controller sau khi load FXML
        StartGameController controller = fxmlLoader.getController();

        // Tạo danh sách bricks rồi truyền vào LoadBrick()
        GameManager gameManager = new GameManager();
        gameManager.startGame(controller);

        // 🔹 Khi người chơi tắt cửa sổ => lưu điểm
        stage.setOnCloseRequest(event -> {
            // Đảm bảo không gọi 2 lần nếu gameOver đã chạy
            if (gameManager.isGameState()) {
                gameManager.gameOver(controller);
            }
        });

        stage.setTitle("Arkanoid Game");
        stage.sizeToScene();
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}