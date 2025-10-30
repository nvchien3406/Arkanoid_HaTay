package GameController;
import Models.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Paddle paddle;
    private Ball ball;
    private List<Brick> listBricks;
    private List<PowerUp> listPowerUps;
    private AnimationTimer gameTimer;
    private int score ;
    private int lives;
    private boolean gameState;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean isGameState() {
        return gameState;
    }

    public void setGameState(boolean gameState) {
        this.gameState = gameState;
    }

    public void startGame(StartGameController controller) {
        score = 0;
        lives = 3;
        gameState = true;

        // 🔹 Khởi tạo paddle & ball
//        paddle = new Paddle(550, 600, 100, 20, 10, 0, StartGameController.paddleImages[0]);
//        ball = new Ball(550, 500, 20, 20, StartGameController.BallImages[0], 0.1, 1, -1);

        // 🔹 Load đối tượng lên màn
        this.listBricks = controller.LoadBrick();
        this.paddle = controller.LoadPaddle();
        this.ball = controller.LoadBall();

        // 🔹 Lấy Scene để bắt phím
        Scene scene = controller.getStartGame().getScene();
        if (scene != null) {
            setupKeyControls(scene);
        } else {
            // Nếu Scene chưa sẵn sàng (gặp khi load FXML), gắn listener
            controller.getStartGame().sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) setupKeyControls(newScene);
            });
        }

        // 🔹 Bắt đầu vòng lặp game
        startGameLoop();
    }

    private void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) paddle.moveL = true;
            if (event.getCode() == KeyCode.RIGHT) paddle.moveR = true;
            if (event.getCode() == KeyCode.SPACE) ball.setStanding(false);
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) paddle.moveL = false;
            if (event.getCode() == KeyCode.RIGHT) paddle.moveR = false;
        });

        // Bảo đảm focus để nhận phím
        scene.getRoot().requestFocus();
    }


    public void updateGame(){
        ball.moveBallWithPaddle(paddle);
        paddle.movePaddle();
        //ball.checkCollision(paddle);
        ball.checkPaddleCollision(paddle);
        ball.checkBrickCollision(listBricks);
        ball.checkWallCollision(paddle);
    }

    public void handelInput(){

    }

    public boolean checkCollisions(){
        return true;
    }

    public void gameOver(){
        paddle = null;
        ball = null;
        score = 0;
        lives = 0;
        gameState = false;
    }

    private void startGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateGame();
            }
        };
        gameTimer.start();
    }
}
