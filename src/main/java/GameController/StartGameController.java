package GameController;

import Models.*;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartGameController implements GameConstant{
    @FXML
    private AnchorPane startGamePane;
    @FXML
    private ListView<String> scoreBoard;
    @FXML
    private Label Score, TopScore, Level;
    @FXML
    private Button Pause;

//    public static final int ROWS = 14;
//    public static final int COLS = 18;
//    public static final int BRICK_WIDTH = 32;
//    public static final int BRICK_HEIGHT = 16;
//    public static final Pair<String, String>[] brickImages = new Pair[]{
//            new Pair<>("/image/BlueBrick.png", "NormalBrick"),
//            new Pair<>("/image/GreenBrick.png", "NormalBrick"),
//            new Pair<>("/image/OrangeBrick.png", "NormalBrick"),
//            new Pair<>("/image/PurpleBrick.png", "StrongBrick"),
//            new Pair<>("/image/RedBrick.png", "StrongBrick"),
//            new Pair<>("/image/YellowBrick.png", "StrongBrick"),
//            new Pair<>("/image/SpecialBrick.png", "SpecialBrick")
//    };
//    public static final String[] paddleImages = {
//            "/image/Paddle.png"
//    };
//    public static final String[] BallImages = {
//            "/image/Ball.png"
//    };
//    public static final String[] powerUpImages = {
//            "/image/ExpandPaddlePowerUp.png"
//    };

    @FXML
    public List<Brick> LoadBrick() {
        List<Brick> bricks = new ArrayList();
        Random random = new Random();

        int[][] pattern = {
                {5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 4, 4, 1, 1, 4, 4, 1, 1, 1, 1, 3, 3, 1, 1, 3, 3, 1},
                {1, 1, 1, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1},
                {1, 1, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 1, 1},
                {1, 4, 6, 4, 4, 6, 4, 1, 1, 1, 1, 3, 6, 3, 3, 6, 3, 1},
                {4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3},
                {4, 1, 4, 4, 4, 4, 1, 4, 1, 1, 3, 1, 3, 3, 3, 3, 1, 3},
                {1, 1, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 1, 1},
                {1, 1, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2}

        };

        for (int row = ROWS - 1; row >= 0; row--) {
            for (int col = 0; col < COLS; col++) {

                // Tạo ngẫu nhiên: 20% không có gạch
                //if (random.nextDouble() < 0.2) continue;

                double x = col * BRICK_WIDTH + 62;
                double y = row * BRICK_HEIGHT + 50;

                String imgPath = brickImages[pattern[row][col]].getKey();

                Brick brick;
                if (brickImages[pattern[row][col]].getValue().equals("NormalBrick")) {
                    brick = new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, imgPath);
                } else if (brickImages[pattern[row][col]].getValue().equals("StrongBrick")) {
                    brick = new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, imgPath);
                } else brick = new SpecialBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, imgPath);

                bricks.add(brick);
                startGamePane.getChildren().add(brick.getImageView());
            }
        }
        return bricks;
    }

    @FXML
    public Paddle LoadPaddle() {
        double width = PADDLE_WIDTH;
        double height = PADDLE_HEIGHT;
        double startX = 550;
        double startY = 600;
        Paddle paddle = new Paddle(startX, startY, width, height, paddleImages[0], 0, 0,
                PADDLE_SPEED, false, false);

        startGamePane.getChildren().add(paddle.getImageView());
        return paddle;
    }

    @FXML
    public Ball LoadBall() {
        double size = BALL_SIZE;

        double startX = 550;
        double startY = 500;

        Ball ball = new Ball(startX , startY , 20 , 20 , BallImages[0] ,BALL_SPEED,1 , 1 );
        startGamePane.getChildren().add(ball.getImageView());
        return ball;
    }

    public AnchorPane getStartGamePane() {
        return startGamePane;
    }

    // Cập nhật điểm hiện tại
    public void updateCurrentScore(int score) {
        Score.setText(String.valueOf(score));
    }

    // Hiển thị danh sách top 10
    public void updateHighScores(List<String> topScores) {
        scoreBoard.getItems().setAll(topScores);
    }

    public AnchorPane getStartGame() {
        return startGamePane;
    }
}
