package GameController.Controllers;

import GameController.Manager.GameManager;
import Models.Player.Player;
import Utils.SceneTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginController {
    @FXML
    private TextField txtUsername;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();

        if (!username.isEmpty()) {
            Player player = new Player(username, 0, 3);
            GameManager.getInstance().getObjectManager().setPlayer(player);

            Stage stage = (Stage) txtUsername.getScene().getWindow();
            SceneTransition.switchScene(stage, "startGame/startGame.fxml");
        }
    }
}
