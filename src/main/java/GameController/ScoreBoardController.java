package GameController;

import Models.Player;
import Utils.SceneTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class ScoreBoardController {
    @FXML
    private TableView<Player> scoreTable;
    @FXML
    private TableColumn<Player ,String> nameColumn;
    @FXML
    private TableColumn<Player ,Integer> rankColumn;
    @FXML
    private TableColumn<Player, Integer> scoreColumn;
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        List<Player> topPlayers = ScoreDAO.getTopPlayers();
        scoreTable.getItems().addAll(topPlayers);

        backButton.setOnAction(e -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            SceneTransition.switchScene(stage, "menuGame.fxml");
        });
    }
}
