package GameController;

import DAO.IScoreRepository;
import DAO.SQLiteScoreRepository;
import GameController.Controllers.SettingsController;
import GameController.Manager.GameManager;
import GameController.Manager.ISoundService;
import GameController.Manager.SoundManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // 🔹 Khởi động vào menu chính
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/menuGame/menuGame.fxml"));
            Scene scene = new Scene(loader.load());

            // 🔹 Load CSS (nếu có)
            scene.getStylesheets().add(Main.class.getResource("/Views/menuGame/menuGame.css").toExternalForm());

            IScoreRepository repo = new SQLiteScoreRepository("data/scores.db");
            ISoundService soundService = new SoundManager();

            GameManager.initialize(repo ,soundService);

            SettingsController.LoadSettings();

            stage.setTitle("Arkanoid Game");
            stage.sizeToScene();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}