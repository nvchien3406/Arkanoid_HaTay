package GameController;
import Models.*;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private Paddle paddle;
    private Ball ball;
    private List<Brick> listBricks= new ArrayList();
    private List<PowerUp> listPowerUps= new ArrayList();
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

    public void startGame(StartGameController controller){
        score = 0;
        lives = 3;
        gameState = true;

        // 🔹 Khởi tạo paddle & ball trước khi dùng
        paddle = new Paddle(550, 600, 100, 20, 10, 0, StartGameController.paddleImages[0]);
        ball = new Ball(550, 500, 20, 20, StartGameController.BallImages[0], 15, 1, 1);

        // 🔹 Gọi các hàm load
        controller.LoadBrick(listBricks);
        controller.LoadPaddle(paddle);
        controller.LoadBall(ball);

        // 🔹 Bắt đầu vòng game
        startGameLoop();
    }


    public void updateGame(){
        ball.moveBall();
        ball.checkCollision(paddle);
        ball.checkWallCollision();
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
