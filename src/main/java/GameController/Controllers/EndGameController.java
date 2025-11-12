package GameController.Controllers;

import GameController.Manager.GameManager;
import Utils.SceneTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EndGameController {
    @FXML
    private AnchorPane endGamePane;
    @FXML
    private Label gameOver;
    @FXML
    private Label finalScore ;
    @FXML
    private Label rank;
    @FXML
    private Button replay;
    @FXML
    private Button Menu;

    public void initialize() {
        GameManager.getInstance().getSoundService().playLosing();

        replay.setOnAction(e -> {
            Stage stage =  (Stage) replay.getScene().getWindow();
            SceneTransition.switchScene(stage, "startGame/startGame.fxml");
        });

        Menu.setOnAction(e -> {
            Stage stage =  (Stage) Menu.getScene().getWindow();
            SceneTransition.switchScene(stage, "menuGame/menuGame.fxml");
        });
    }

    public void setFinalScore(int score) {
        finalScore.setText("Final Score: " + String.valueOf(score));
    }

    public void setRank(String rank) {
        this.rank.setText("Rank: " + rank );
    }
}