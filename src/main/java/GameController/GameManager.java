package GameController;
import Models.*;
import Utils.SceneTransition;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    private Paddle paddle;
    private List<Ball> listBalls = new ArrayList<>();
    private List<Brick> listBricks;
    private List<PowerUp> listPowerUps = new ArrayList<>();
    private AnimationTimer gameTimer;
    private Player player ;
    private ScoreDAO scoreDAO;
    private boolean gameState;

    // === Deferred removal lists ===
    private final List<Ball> ballsToRemove = new ArrayList<>();
    private final List<PowerUp> powerUpsToRemove = new ArrayList<>();
    private final List<PowerUp> powerUpsToAdd = new ArrayList<>(); // in case you want deferred add

    // 🔒 Constructor private: chỉ cho phép tạo nội bộ
    private GameManager() {
        listPowerUps = new ArrayList<>();
    }

    // 🔹 Singleton getter
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public static void setInstance(GameManager instance) {
        GameManager.instance = instance;
    }

    public List<Ball> getListBalls() {
        return listBalls;
    }

    public void setListBalls(List<Ball> listBalls) {
        this.listBalls = listBalls;
    }

    public AnimationTimer getGameTimer() {
        return gameTimer;
    }

    public void setGameTimer(AnimationTimer gameTimer) {
        this.gameTimer = gameTimer;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ScoreDAO getScoreDAO() {
        return scoreDAO;
    }

    public void setScoreDAO(ScoreDAO scoreDAO) {
        this.scoreDAO = scoreDAO;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public List<Brick> getListBricks() {
        return listBricks;
    }

    public void setListBricks(List<Brick> listBricks) {
        this.listBricks = listBricks;
    }

    public List<PowerUp> getListPowerUps() {
        return listPowerUps;
    }

    public void setListPowerUps(List<PowerUp> listPowerUps) {
        this.listPowerUps = listPowerUps;
    }

    public boolean isGameState() {
        return gameState;
    }

    public void setGameState(boolean gameState) {
        this.gameState = gameState;
    }

    // === Deferred operations API ===
    public void markBallForRemoval(Ball b) {
        if (b != null && !ballsToRemove.contains(b)) ballsToRemove.add(b);
    }

    public void markPowerUpForRemoval(PowerUp p) {
        if (p != null && !powerUpsToRemove.contains(p)) powerUpsToRemove.add(p);
    }

    public void queuePowerUpToAdd(PowerUp p) {
        if (p != null) powerUpsToAdd.add(p);
    }

    // Remove powerUp immediately helper (kept for external calls)
    public void removePowerUp(PowerUp powerUp) {
        if (listPowerUps != null && listPowerUps.contains(powerUp)) {
            listPowerUps.remove(powerUp);
            if (powerUp.getImageView() != null) {
                powerUp.getImageView().setVisible(false);
            }
        }
    }

    public void pauseGame() {
        if (gameTimer != null) gameTimer.stop();
    }

    public void resumeGame(StartGameController controller) {
        if (gameTimer != null){
            startGameLoop(controller);
        }
    }

    public void startGame(StartGameController controller) {
        player = new Player("Bao" ,0 , 3);
        scoreDAO = new ScoreDAO();
        gameState = true;

        this.listBricks = controller.LoadBrick();
        this.paddle = controller.LoadPaddle();

        // ✅ Chỉ gọi 1 lần
        controller.LoadBall();

        // 🔹 Lấy Scene để bắt phím
        Scene scene = controller.getStartGamePane().getScene();
        if (scene != null) {
            setupKeyControls(scene);
        } else {
            controller.getStartGamePane().sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) setupKeyControls(newScene);
            });
        }

        // 🔹 Bắt đầu vòng lặp game
        startGameLoop(controller);
    }

    public void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.LEFT) paddle.moveL = true;
            if (event.getCode() == javafx.scene.input.KeyCode.RIGHT) paddle.moveR = true;
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                for (Ball ball : listBalls) {
                    if (ball.isStanding()) {
                        ball.setStanding(false);
                        ball.setDirectionX((Math.random() < 0.5 ? -(0.4 + Math.random() * 0.6) : 0.4 + Math.random() * 0.));
                        ball.setDirectionY(-1);
                    }
                }
            };
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.LEFT) paddle.moveL = false;
            if (event.getCode() == javafx.scene.input.KeyCode.RIGHT) paddle.moveR = false;
        });

        // Bảo đảm focus để nhận phím
        scene.getRoot().requestFocus();
    }

    public void disableKeyControls(Scene scene) {
        scene.setOnKeyPressed(null);
        scene.setOnKeyReleased(null);
    }

    // ================= main update =================
    public void updateGame(StartGameController controller){
        // 1) xử lý va chạm & cập nhật vật thể (dùng bản sao để an toàn)
        checkCollisions();

        // 2) di chuyển bóng
        for (Ball ball : new ArrayList<>(listBalls)) {
            ball.moveBallWithPaddle(paddle);
        }

        // 3) di chuyển paddle và cập nhật UI
        paddle.movePaddle(controller);
        controller.updateCurrentScore(player.getScore());
        List<String> topscores = scoreDAO.getHighScores();
        controller.updateHighScores(topscores);

        // 4) update powerups (dùng bản sao)
        if (listPowerUps != null && !listPowerUps.isEmpty()) {
            for (PowerUp p : new ArrayList<>(listPowerUps)) {
                p.update();
                p.checkPaddleCollision(paddle);
            }
            // collect expired
            for (PowerUp p : new ArrayList<>(listPowerUps)) {
                if (p.isExpired()) markPowerUpForRemoval(p);
            }
        }

        // 5) dọn dẹp deferred removes / thêm deferred adds
        cleanupDeferred();
    }

    public void handelInput(){
        // (nếu cần tách input handling)
    }

    // ===== checkCollisions: duyệt bằng bản sao để tránh concurrent problem =====
    public void checkCollisions(){
        // iterate copy => an toàn nếu Ball/PowerUp đánh dấu để remove
        for (Ball ball : new ArrayList<>(listBalls)) {
            ball.checkPaddleCollision(paddle);
            ball.checkBrickCollision(listBricks, player);
            ball.checkWallCollision(paddle, player);
        }
    }

    // ===== cleanup deferred removals & spawn ball if needed =====
    private void cleanupDeferred() {
        // 1) Thêm powerups queued (nếu có)
        if (!powerUpsToAdd.isEmpty()) {
            listPowerUps.addAll(powerUpsToAdd);
            powerUpsToAdd.clear();
        }

        // 2) Xóa powerups deferred
        if (!powerUpsToRemove.isEmpty()) {
            for (PowerUp p : powerUpsToRemove) {
                listPowerUps.remove(p);
                if (p.getImageView() != null) p.getImageView().setVisible(false);
            }
            powerUpsToRemove.clear();
        }

        // 3) Xóa balls deferred (đảm bảo xóa sau khi vòng lặp xong)
        if (!ballsToRemove.isEmpty()) {
            for (Ball b : ballsToRemove) {
                listBalls.remove(b);
                if (b.getImageView() != null) b.getImageView().setVisible(false);
            }
            ballsToRemove.clear();

            // Nếu KHÔNG còn bóng nào trên màn hình -> spawn 1 bóng mới trên paddle
            if (listBalls.isEmpty()) {
                spawnBallOnPaddleAndLoseLife();
            }
        }
    }

    // Tạo 1 quả bóng mới ở giữa paddle và trừ 1 mạng
    private void spawnBallOnPaddleAndLoseLife() {
        if (paddle == null || player == null) return;

        Ball newBall = new Ball(
                paddle.getX() + paddle.getWidth() / 2 - 10,
                paddle.getY() - 20,
                20, 20,
                StartGameController.BallImages[0],
                3, 0, -1
        );
        newBall.setStanding(true);
        listBalls.add(newBall);

        // add to scene graph
        AnchorPane pane = (AnchorPane) paddle.getImageView().getParent();
        pane.getChildren().add(newBall.getImageView());

        // trừ mạng
        player.setLives(player.getLives() - 1);
    }

    public void gameOver(StartGameController controller){
        paddle = null;
        // remove nodes and clear lists
        for (Ball ball : listBalls) {
            if (ball.getImageView() != null) ball.getImageView().setVisible(false);
        }
        listBalls.clear();

        gameState = false;
        if (gameTimer != null) gameTimer.stop();
        disableKeyControls(controller.getStartGamePane().getScene());
        scoreDAO.insertScore(player.getPlayerName(),  player.getScore());
        List<String> topscores = scoreDAO.getHighScores();
        controller.updateHighScores(topscores);

        EndGameController endGameController = SceneTransition.switchSceneWithController(controller.getStage(), "endGame.fxml");
        endGameController.setFinalScore(player.getScore());
        endGameController.setRank(scoreDAO.getRankPlayer(player));

        player = null;
    }

    public boolean hasActivePowerUp() {
        for (PowerUp p : listPowerUps) {
            if (p.isActive() && !p.isExpired()) {
                return true;
            }
        }
        return false;
    }

    private void startGameLoop(StartGameController controller) {
        // 🔹 Lấy Scene để bắt phím
        Scene scene = controller.getStartGamePane().getScene();
        if (scene != null) {
            setupKeyControls(scene);
        } else {
            // Nếu Scene chưa sẵn sàng (gặp khi load FXML), gắn listener
            controller.getStartGamePane().sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) setupKeyControls(newScene);
            });
        }

        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(player.playerIsAlive()){
                    updateGame(controller);
                }
                else{
                    gameOver(controller);
                }
            }
        };
        gameTimer.start();
    }
}
