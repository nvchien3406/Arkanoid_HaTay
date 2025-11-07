package GameController;

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
        btnStart.setOnAction(e -> switchScene("startGame.fxml"));
        btnScore.setOnAction(e -> switchScene("ScoreBoard.fxml"));
        btnExit.setOnAction(e -> System.exit(0));
    }

    private void switchScene(String fxmlFile) {
        try {
            Stage stage = (Stage) btnStart.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/GameController/" + fxmlFile));

            Scene scene = new Scene(root);

            // 🔹 Lấy tên CSS tương ứng với file FXML (nếu có)
            String cssName = fxmlFile.replace(".fxml", ".css");
            var cssUrl = getClass().getResource("/GameController/" + cssName);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

