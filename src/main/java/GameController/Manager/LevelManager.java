package GameController.Manager;

import GameController.Controllers.StartGameController;
import GameController.GameConstants.GameConstant;
import Models.Brick.Brick;
import Models.Level.LevelGame;   // ⚠️ thêm dòng này

import java.util.List;

public class LevelManager {
    private final ObjectManager objectManager;
    private final GameManager gameManager;
    private final GameUIManager gameUIManager;

    public LevelManager(ObjectManager objectManager, GameManager gameManager, GameUIManager gameUIManager) {
        this.objectManager = objectManager;
        this.gameManager = gameManager;
        this.gameUIManager = gameUIManager;
    }

    public boolean allBricksDestroyed() {
        List<Brick> bricks = objectManager.getListBricks();
        if (bricks == null || bricks.isEmpty()) return false;
        return bricks.stream().allMatch(Brick::isDestroyed);
    }

    public void handleNextLevel(StartGameController controller) {
        LevelGame level = gameManager.getCurrentLevel();
        if (level == null) {
            System.err.println("[LevelManager] LevelGame null");
            return;
        }

        level.nextLevel();
        controller.animateLevelUp(level.getLevelNumber());
        clearLevelObjects(controller);

        objectManager.setListBricks(controller.LoadBrick(level));
        objectManager.setPaddle(controller.LoadPaddle());
        controller.LoadBall();
        controller.loadBackground(level.getLevelNumber());

        gameUIManager.showLevelIntro(controller, level.getLevelNumber());

        var scene = controller.getStartGamePane().getScene();
        if (scene != null) gameManager.getInputManager().setupKeyControls(scene);
    }

    public void clearLevelObjects(StartGameController controller) {
        objectManager.clearLevelObjects(controller);
    }
}
