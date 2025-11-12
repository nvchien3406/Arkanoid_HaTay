package GameController.Controllers;

import Utils.SceneTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MainMenuController {

    @FXML private Button btnStart;
    @FXML private Button btnScore;
    @FXML private Button btnSettings;
    @FXML private Button btnExit;
    @FXML private MediaView mediaView;

    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        //SoundManager.StopSoundBackground();
        //SoundManager.PlaySoundMenuBackground();
        try {
            String videoPath = getClass().getResource("/images/Background.mp4").toExternalForm();
            System.out.println("Video path: " + videoPath);
            Media media = new Media(videoPath);
            mediaPlayer = new MediaPlayer(media);

            // Thiết lập MediaView
            mediaView.setMediaPlayer(mediaPlayer);

            // Lặp vô hạn và tự động phát
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setOnReady(() -> mediaPlayer.play());

            // Đưa MediaView ra phía sau (làm nền)
            mediaView.toBack();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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


