package GameController;

import Utils.SceneTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class MainMenuController {

    @FXML private Button btnStart;
    @FXML private Button btnScore;
    @FXML private Button btnSettings;
    @FXML private Button btnExit;

    @FXML
    public void initialize() {
        btnStart.setOnAction(e -> {
            Stage stage = (Stage) btnStart.getScene().getWindow();
            SceneTransition.switchScene(stage, "startGame.fxml");
        });
        btnScore.setOnAction(e -> {
            Stage stage = (Stage) btnStart.getScene().getWindow();
            SceneTransition.switchScene(stage, "ScoreBoard.fxml");
        });
        btnExit.setOnAction(e -> System.exit(0));
    }

}

