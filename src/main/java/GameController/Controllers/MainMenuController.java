package GameController.Controllers;

import Utils.SceneTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MainMenuController {

    @FXML private Button btnStart;
    @FXML private Button btnScore;
    @FXML private Button btnSettings;
    @FXML private Button btnExit;

    @FXML
    public void initialize() {
        //SoundManager.StopSoundBackground();
        //SoundManager.PlaySoundMenuBackground();

        btnStart.setOnAction(e -> {
            //SoundManager.PlayMouseClick();

            Stage stage = (Stage) btnStart.getScene().getWindow();
            SceneTransition.switchScene(stage, "login/login.fxml");
        });

        btnScore.setOnAction(e -> {
            Stage stage = (Stage) btnStart.getScene().getWindow();
            SceneTransition.switchScene(stage, "scoreBoard/scoreBoard.fxml");

            //SoundManager.PlayMouseClick();
        });

        btnSettings.setOnAction(e -> {
            Stage stage = (Stage) btnStart.getScene().getWindow();

            // 1. Lấy Scene gốc
            Scene currentScene = stage.getScene();
            SettingsController.setBackScene(currentScene);

            SceneTransition.switchScene(stage, "settingGame/settings.fxml");
        });

        btnExit.setOnAction(e -> {
            //SoundManager.PlayMouseClick();

            System.exit(0);
        });
    }

}


