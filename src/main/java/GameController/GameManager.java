package GameController;
import Models.*;
import Utils.SceneTransition;
import javafx.animation.AnimationTimer;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.text.Text;
import javafx.util.Duration;


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
    private Line aimingArrow;
    private static final double AIMING_ARROW_LENGTH = 80.0;

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

    public void resetGameManager(StartGameController controller) {
        // 1️⃣ Dừng game loop
        if (gameTimer != null) {
            gameTimer.stop();
            gameTimer = null;
        }

        // 2️⃣ Disable input để tránh key/mouse cũ
        if (controller != null && controller.getStartGamePane() != null) {
            Scene scene = controller.getStartGamePane().getScene();
            if (scene != null) disableKeyControls(scene);
        }

        // 3️⃣ Xóa tất cả balls
        if (listBalls != null) {
            for (Ball b : listBalls) {
                if (b.getImageView() != null) {
                    ((AnchorPane) b.getImageView().getParent()).getChildren().remove(b.getImageView());
                }
            }
            listBalls.clear();
        }

        // 4️⃣ Xóa tất cả bricks
        if (listBricks != null) {
            for (Brick brick : listBricks) {
                if (brick.getImageView() != null) {
                    ((AnchorPane) brick.getImageView().getParent()).getChildren().remove(brick.getImageView());
                }
            }
            listBricks.clear();
        }

        // 5️⃣ Xóa tất cả powerups
        if (listPowerUps != null) {
            for (PowerUp p : listPowerUps) {
                if (p.getImageView() != null) {
                    ((AnchorPane) p.getImageView().getParent()).getChildren().remove(p.getImageView());
                }
            }
            listPowerUps.clear();
        }

        ballsToRemove.clear();
        powerUpsToRemove.clear();
        powerUpsToAdd.clear();

        // 6️⃣ Xóa paddle
        if (paddle != null && paddle.getImageView() != null) {
            ((AnchorPane) paddle.getImageView().getParent()).getChildren().remove(paddle.getImageView());
        }
        paddle = null;

        // 7️⃣ Xóa aiming arrow
        if (aimingArrow != null && controller != null && controller.getStartGamePane() != null) {
            controller.getStartGamePane().getChildren().remove(aimingArrow);
        }
        aimingArrow = null;

        // 8️⃣ Reset player & score DAO
        player = null;
        if (scoreDAO != null) {
            scoreDAO = null;
        }

        // 9️⃣ Remove tất cả scoreboard/highscore Text nodes (nếu có)
        if (controller != null && controller.getStartGamePane() != null) {
            controller.getStartGamePane().getChildren().removeIf(node -> node instanceof Text);
        }
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

        //SoundManager.StopSoundMenuBackground();
        SoundManager.PlaySoundBackground();

        this.listBricks = controller.LoadBrick();
        this.paddle = controller.LoadPaddle();

        aimingArrow = new Line();
        aimingArrow.setStrokeWidth(3);
        aimingArrow.setStroke(Color.CYAN);
        aimingArrow.setVisible(false);
        controller.getStartGamePane().getChildren().add(aimingArrow);
        // ✅ Chỉ gọi 1 lần
        controller.LoadBall();

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

        scene.setOnMousePressed(event -> {
            for (Ball ball : listBalls) {
                // Chỉ ngắm khi bóng đang đứng yên
                if (ball.isStanding()) {
                    // Tính toán tâm quả bóng (giả sử getX/getY là góc trên trái)
                    double ballCenterX = ball.getX() + ball.getWidth() / 2;
                    double ballCenterY = ball.getY() + ball.getHeight() / 2;

                    aimingArrow.setStartX(ballCenterX);
                    aimingArrow.setStartY(ballCenterY);
                    updateAimingArrow(event.getX(), event.getY());
                    aimingArrow.setVisible(true);
                }
            }
        });

        scene.setOnMouseDragged(event -> {
            // Chỉ cập nhật khi đang ngắm (mũi tên hiển thị)
            if (aimingArrow.isVisible()) {
                updateAimingArrow(event.getX(), event.getY());
            }
        });

        scene.setOnMouseReleased(event -> {
            // Chỉ bắn khi đang ngắm
            if (aimingArrow.isVisible()) {
                for (Ball ball : listBalls) {
                    aimingArrow.setVisible(false); // Ẩn mũi tên

                    // Tính toán tâm quả bóng
                    double ballCenterX = ball.getX() + ball.getWidth() / 2;
                    double ballCenterY = ball.getY() + ball.getHeight() / 2;

                    // Tính vector hướng
                    double deltaX = event.getX() - ballCenterX;
                    double deltaY = event.getY() - ballCenterY;

                    // Luôn ép bóng bay lên (deltaY phải là số âm)
                    if (deltaY >= 0) {
                        deltaY = -0.1; // Một giá trị nhỏ để tránh lỗi, nếu chỉ click
                    }

                    // Tính độ dài vector (Pythagoras)
                    double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                    // Chuẩn hóa vector (để có tốc độ không đổi)
                    double normX = deltaX / magnitude;
                    double normY = deltaY / magnitude;

                    // Dựa trên code cũ của bạn, có vẻ setDirectionX/Y là vector hướng
                    ball.setStanding(false);
                    ball.setDirectionX(normX);
                    ball.setDirectionY(normY);
                }
            }
        });

        // Bảo đảm focus để nhận phím
        scene.getRoot().requestFocus();
    }

    public void disableKeyControls(Scene scene) {
        scene.setOnKeyPressed(null);
        scene.setOnKeyReleased(null);
        scene.setOnMousePressed(null);
        scene.setOnMouseDragged(null);
        scene.setOnMouseReleased(null);
    }


    public void updateGame(StartGameController controller){
        // 1) xử lý va chạm & cập nhật vật thể (dùng bản sao để an toàn)
        checkCollisions(controller);

        // 2) di chuyển bóng
        for (Ball ball : new ArrayList<>(listBalls)) {
            ball.moveBallWithPaddle(paddle);
        }
        paddle.movePaddle(controller);
        controller.updateCurrentScore(player.getScore());
        List<String> topscores = scoreDAO.getHighScores();
        controller.updateHighScores(topscores);

        controller.updateCurrentTopScore(ScoreDAO.getTopScores());

        paddle.movePaddle(controller);

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
        cleanupDeferred(controller);
    }

    public void handelInput(){

    }

    public void checkCollisions(StartGameController controller){
        for (Ball ball : new ArrayList<>(listBalls)){
            ball.checkPaddleCollision(paddle);
            ball.checkBrickCollision(listBricks , player,controller);
            ball.checkWallCollision(paddle , player);
        }
    }

    // ===== cleanup deferred removals & spawn ball if needed =====
    private void cleanupDeferred(StartGameController controller) {
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
                spawnBallOnPaddleAndLoseLife(controller);
            }
        }
    }

    // Tạo 1 quả bóng mới ở giữa paddle và trừ 1 mạng
    private void spawnBallOnPaddleAndLoseLife(StartGameController controller) {
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

//        controller.getStartGamePane().getChildren().add(powerUp.getImageView());
//        Node pauseMenu = controller.getStartGamePane().lookup("#pauseMenu");
//        if (pauseMenu != null) pauseMenu.toFront();

        controller.getStartGamePane().getChildren().add(newBall.getImageView());
        Node pauseMenu = controller.getStartGamePane().lookup("#pauseMenu");
        if (pauseMenu != null) pauseMenu.toFront();

//        // add to scene graph
//        AnchorPane pane = (AnchorPane) paddle.getImageView().getParent();
//        pane.getChildren().add(newBall.getImageView());

        // trừ mạng
        player.setLives(player.getLives() - 1);
    }

    public void gameOver(StartGameController controller){
        scoreDAO.insertScore(player.getPlayerName(),  player.getScore());
        List<String> topscores = scoreDAO.getHighScores();
        controller.updateHighScores(topscores);

        EndGameController endGameController = SceneTransition.switchSceneWithController(controller.getStage(), "endGame.fxml");
        endGameController.setFinalScore(player.getScore());
        endGameController.setRank(scoreDAO.getRankPlayer(player));

        player = null;
        scoreDAO = null;

        resetGameManager(controller);
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

    /**
     * Cập nhật vị trí cuối của mũi tên dựa trên vị trí chuột,
     * nhưng giữ nguyên độ dài cố định (AIMING_ARROW_LENGTH).
     */
    private void updateAimingArrow(double mouseX, double mouseY) {
        // Lấy điểm bắt đầu (tâm quả bóng)
        double startX = aimingArrow.getStartX();
        double startY = aimingArrow.getStartY();

        // 1. Tính vector thô
        double deltaX = mouseX - startX;
        double deltaY = mouseY - startY;

        // 2. Ép mũi tên luôn hướng lên
        if (deltaY >= 0) {
            deltaY = -0.1; // Một giá trị âm nhỏ để tránh lỗi chia cho 0
            if (deltaX == 0) deltaX = 0.01; // Tránh trường hợp click ngay bên dưới
        }

        // 3. Tính độ dài (magnitude)
        double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // 4. Chuẩn hóa vector (lấy hướng)
        double normX, normY;
        if (magnitude == 0) {
            normX = 0;
            normY = -1; // Nếu không di chuyển, mặc định hướng thẳng lên
        } else {
            normX = deltaX / magnitude;
            normY = deltaY / magnitude;
        }

        // 5. Tính điểm cuối mới dựa trên độ dài cố định
        double endX = startX + normX * AIMING_ARROW_LENGTH;
        double endY = startY + normY * AIMING_ARROW_LENGTH;

        // 6. Cập nhật đường thẳng
        aimingArrow.setEndX(endX);
        aimingArrow.setEndY(endY);
    }

    public void showScorePopup(StartGameController controller, double x, double y, int score) {
        Text scoreText = new Text("+" + score);
        scoreText.setFill(Color.GOLD);
        scoreText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        scoreText.setLayoutX(x);
        scoreText.setLayoutY(y);

        controller.getStartGamePane().getChildren().add(scoreText);

        ScaleTransition scale = new ScaleTransition(Duration.millis(400), scoreText);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.5);
        scale.setToY(1.5);

        FadeTransition fade = new FadeTransition(Duration.millis(800), scoreText);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        TranslateTransition moveUp = new TranslateTransition(Duration.millis(800), scoreText);
        moveUp.setByY(-40);

        ParallelTransition anim = new ParallelTransition(scale, fade, moveUp);
        anim.setOnFinished(e -> controller.getStartGamePane().getChildren().remove(scoreText));
        anim.play();
    }

}
