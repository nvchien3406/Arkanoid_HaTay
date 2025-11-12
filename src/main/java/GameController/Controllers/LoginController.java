package GameController.Controllers;

import GameController.Manager.GameManager;
import Models.Player.Player;
import Utils.SceneTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController {
    @FXML
    public Button btnStart;
    @FXML
    private TextField txtName;
    @FXML
    private Label lblWarning;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtName.getText().trim();

        if (!username.isEmpty()) {
            Player player = new Player(username, 0, 3);
            GameManager.getInstance().getObjectManager().setPlayer(player);

            Stage stage = (Stage) txtName.getScene().getWindow();
            SceneTransition.switchScene(stage, "startGame/startGame.fxml");

            lblWarning.setText("");
        }
        else{
            lblWarning.setText("Please enter your name before starting!");
        }
    }
}
