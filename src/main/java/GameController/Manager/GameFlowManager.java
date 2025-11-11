package GameController.Manager;

import GameController.EndGameController;
import GameController.StartGameController;
import Utils.SceneTransition;
import java.util.List;

public class GameFlowManager {
    private final GameManager gameManager;
    private boolean gameState;

    public GameFlowManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean isGameState() { return gameState; }
    public void setGameState(boolean gameState) { this.gameState = gameState; }

    public void pauseGame() {
        if (gameManager.getGameTimer() != null) gameManager.getGameTimer().stop();
    }

    public void resumeGame(StartGameController controller) {
        if (gameManager.getGameTimer() != null) {
            gameManager.startGameLoop(controller);
        }
    }

    public void gameOver(StartGameController controller){
        var player = gameManager.getObjectManager().getPlayer();
        if (player != null) {
            gameManager.getScoreDAO().insertScore(player.getPlayerName(), player.getScore());
            List<String> topscores = gameManager.getScoreDAO().getHighScores();
            controller.updateHighScores(topscores);

            EndGameController endGameController = SceneTransition.switchSceneWithController(controller.getStage(), "endGame.fxml");
            endGameController.setFinalScore(player.getScore());
            endGameController.setRank(gameManager.getScoreDAO().getRankPlayer(player));

            gameManager.resetGameManager(controller,false);
        } else {
            // fallback
            gameManager.resetGameManager(controller,false);
        }
    }
}
