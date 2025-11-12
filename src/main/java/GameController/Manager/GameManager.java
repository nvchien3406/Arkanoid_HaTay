package GameController.Manager;

import DAO.IScoreRepository;
import GameController.Controllers.StartGameController;
import Models.Level.LevelGame;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

public class GameManager {
    private static GameManager instance;

    private AnimationTimer gameTimer;
    private final IScoreRepository scoreDAO;
    private final ISoundService soundService;

    // ownership: GameManager tạo và quản lý các manager khác
    private final ObjectManager objectManager;
    private final CollisionManager collisionManager;
    private final GameFlowManager gameFlowManager;
    private final GameUIManager gameUIManager;
    private final InputManager inputManager;
    private final LevelManager levelManager;

    // current model references (do GameManager quản lý luồng)
    private LevelGame currentLevel;

    private GameManager(IScoreRepository scoreDAO, ISoundService soundService) {
        this.scoreDAO = scoreDAO;
        this.soundService = soundService;

        this.objectManager = new ObjectManager();
        this.collisionManager = new CollisionManager(objectManager);
        this.gameFlowManager = new GameFlowManager(this);
        this.gameUIManager = new GameUIManager();
        this.inputManager = new InputManager(objectManager, gameUIManager);
        this.levelManager = new LevelManager(objectManager, this, gameUIManager);
        this.currentLevel = new LevelGame(); // khởi tạo mặc định (hoặc load)
    }

    public static void setInstance(GameManager instance) {
        GameManager.instance = instance;
    }

    public void setGameTimer(AnimationTimer gameTimer) {
        this.gameTimer = gameTimer;
    }

    public CollisionManager getCollisionManager() {
        return collisionManager;
    }

    public GameFlowManager getGameFlowManager() {
        return gameFlowManager;
    }

    public GameUIManager getGameUIManager() {
        return gameUIManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public static void initialize(IScoreRepository repo, ISoundService soundService) {
        if (instance == null) {
            instance = new GameManager(repo, soundService);
        }
    }

    public static GameManager getInstance() {
        if (instance == null) throw new IllegalStateException("GameManager chưa được khởi tạo! Gọi initialize(...) trước.");
        return instance;
    }

    /* ---------------- game lifecycle ---------------- */
    public void startGame(StartGameController controller) {
        gameFlowManager.setGameState(true);

        // play music (soundService là injected vào GameManager)
        if (soundService != null) soundService.playBackground();

        // load objects via controller + current level (LevelManager có thể cung cấp currentLevel)
        objectManager.setListBricks(controller.LoadBrick(currentLevel));
        objectManager.setPaddle(controller.LoadPaddle());
        objectManager.setAimingArrow(gameUIManager.createAimingArrow(controller)); // GameUIManager tạo arrow
        controller.LoadBall();
        gameUIManager.showLevelIntro(controller, currentLevel.getLevelNumber());

        //load background
        controller.loadBackground(currentLevel.getLevelNumber());

        startGameLoop(controller);
    }

    public void updateGame(StartGameController controller) {
        // 1. va chạm
        collisionManager.checkCollisions(controller);

        // 2. cập nhật chuyển động
        objectManager.updateBall();
        objectManager.updatePlayer(controller);
        objectManager.updatePaddle(controller);
        objectManager.updatePowerUps();

        controller.updateLives(objectManager.getPlayer().getLives());

        // 3. DB/Score UI
        List<String> topscores = scoreDAO.getHighScores();
        controller.updateHighScores(topscores);
        controller.updateCurrentTopScore(scoreDAO.getTopScore());

        // 4. cleanup
        objectManager.cleanupDeferred(controller);

        // 5. next level check
        if (levelManager.allBricksDestroyed()) {
            gameTimer.stop();
            PauseTransition wait = new PauseTransition(Duration.seconds(1.0));
            wait.setOnFinished(e -> {
                levelManager.handleNextLevel(controller);
                gameTimer.start();
            });
            wait.play();
        }
    }

    public void startGameLoop(StartGameController controller) {
        Scene scene = controller.getStartGamePane().getScene();
        if (scene != null) {
            inputManager.setupKeyControls(scene);
        } else {
            controller.getStartGamePane().sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) inputManager.setupKeyControls(newScene);
            });
        }

        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (objectManager.getPlayer() != null && objectManager.getPlayer().playerIsAlive()) {
                    updateGame(controller);
                } else {
                    gameFlowManager.gameOver(controller);
                }
            }
        };
        gameTimer.start();
    }

    public void stopGameLoop() {
        if (gameTimer != null) {
            gameTimer.stop();
            gameTimer = null;
        }
    }

    public void resetGameManager(StartGameController controller, boolean keepPlayerData) {
        stopGameLoop();
        levelManager.clearLevelObjects(controller);
        objectManager.clearCollections();

        if (!keepPlayerData) {
            objectManager.getPlayer().resetPlayer();
            currentLevel = new LevelGame();
        }

        if (controller != null && controller.getStartGamePane() != null) {
            controller.getStartGamePane().getChildren().removeIf(node -> node instanceof Text);
        }
    }

    /* ---------------- getters used by other managers ---------------- */
    public IScoreRepository getScoreDAO() { return scoreDAO; }
    public ISoundService getSoundService() { return soundService; }
    public ObjectManager getObjectManager() { return objectManager; }
    public AnimationTimer getGameTimer() { return gameTimer; }
    public LevelGame getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(LevelGame level) { this.currentLevel = level; }
}
