package GameController;
import Models.*;
import javafx.animation.*;
import javafx.scene.Scene;
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
    private Ball ball;
    private List<Brick> listBricks;
    private List<PowerUp> listPowerUps;
    private AnimationTimer gameTimer;
    private Player player ;
    private ScoreDAO scoreDAO;
    private boolean gameState;
    private Line aimingArrow;
    private static final double AIMING_ARROW_LENGTH = 80.0;


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




    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
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

    public void removePowerUp(PowerUp powerUp) {
        if (listPowerUps != null && listPowerUps.contains(powerUp)) {
            // 1. Xóa khỏi danh sách quản lý
            listPowerUps.remove(powerUp);

            // 2. Ẩn hoặc xóa hình ảnh khỏi màn hình (nếu còn hiển thị)
            if (powerUp.getImageView() != null) {
                powerUp.getImageView().setVisible(false);
            }
        }
    }


    public void startGame(StartGameController controller) {
        player = new Player("Bao" ,0 , 10);
        scoreDAO = new ScoreDAO();
        gameState = true;

        // Load đối tượng lên màn
        this.listBricks = controller.LoadBrick();
        this.paddle = controller.LoadPaddle();
        this.ball = controller.LoadBall();

        aimingArrow = new Line();
        aimingArrow.setStrokeWidth(3);
        aimingArrow.setStroke(Color.CYAN);
        aimingArrow.setVisible(false);
        controller.getStartGamePane().getChildren().add(aimingArrow);

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

        // 🔹 Bắt đầu vòng lặp game
        startGameLoop(controller);
    }

    public void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) paddle.moveL = true;
            if (event.getCode() == KeyCode.RIGHT) paddle.moveR = true;
            if (event.getCode() == KeyCode.SPACE) {
                if (ball.isStanding()) {
                    ball.setStanding(false);
                    ball.setDirectionX((Math.random() < 0.5 ? -(0.4 + Math.random() * 0.6) : 0.4 + Math.random() * 0.));
                    ball.setDirectionY(-1);
                }

            };
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) paddle.moveL = false;
            if (event.getCode() == KeyCode.RIGHT) paddle.moveR = false;
        });

        scene.setOnMousePressed(event -> {
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
        checkCollisions(controller);
        ball.moveBallWithPaddle(paddle);
        paddle.movePaddle(controller);
        controller.updateCurrentScore(player.getScore());
        List<String> topscores = scoreDAO.getHighScores();
        controller.updateHighScores(topscores);
        paddle.movePaddle(controller);

        // update powerups và check collision
        if (listPowerUps != null && !listPowerUps.isEmpty()) {
            // update tất cả trước
            for (PowerUp p : new ArrayList<>(listPowerUps)) {
                p.update();                // rơi xuống
                p.checkPaddleCollision(paddle); // ăn vật phẩm
//                if (p.getY() > 800) {
//                    // ẩn/đánh dấu để dọn
//                    p.getImageView().setVisible(false);
//                    p.setCollected(true);  // hoặc set some flag
//                }
            }

            // sau khi update xong, dọn powerup đã expired (đã removeEffect xong)
            List<PowerUp> toRemove = new ArrayList<>();
            for (PowerUp p : listPowerUps) {
                if (p.isExpired()) {
                    toRemove.add(p);
                }
            }
            listPowerUps.removeAll(toRemove);
        }
    }


    public void handelInput(){

    }

    public void checkCollisions(StartGameController controller){
        ball.checkPaddleCollision(paddle);
        ball.checkBrickCollision(listBricks , player, controller);
        ball.checkWallCollision(paddle , player);
    }

    public void gameOver(StartGameController controller){
        paddle = null;
        ball = null;
        gameState = false;
        gameTimer.stop();
        disableKeyControls(controller.getStartGamePane().getScene());
        scoreDAO.insertScore(player.getPlayerName(),  player.getScore());
        List<String> topscores = scoreDAO.getHighScores();
        controller.updateHighScores(topscores);
        player = null;
    }

    private void startGameLoop(StartGameController controller) {
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
