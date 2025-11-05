package GameController;
import Models.Ball;
import Models.Brick;
import Models.NormalBrick;
import Models.Paddle;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartGameController {
    @FXML
    private AnchorPane startGamePane;
    @FXML
    private ListView<String> scoreBoard;
    @FXML
    private Label Score, TopScore, Level;
    @FXML
    private Button Pause;

    public static final int ROWS = 10;
    public static final int COLS = 20;
    public static final int BRICK_WIDTH = 32;
    public static final int BRICK_HEIGHT = 16;
    public static final String[] brickImages = {
            "/image/BlueBrick.png",
            "/image/GreenBrick.png",
            "/image/OrangeBrick.png",
            "/image/PurpleBrick.png",
            "/image/RedBrick.png",
            "/image/YellowBrick.png"
    };
    public static final String[] paddleImages = {
            "/image/Paddle.png"
    };
    public static final String[] BallImages = {
            "/image/Ball.png"
    };

    @FXML
    public List<Brick> LoadBrick() {
        List<Brick> bricks = new ArrayList();
        Random random = new Random();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                // Tạo ngẫu nhiên: 20% không có gạch
                //if (random.nextDouble() < 0.2) continue;

                double x = col * BRICK_WIDTH + 50;
                double y = row * BRICK_HEIGHT + 50;

                String imgPath = brickImages[random.nextInt(brickImages.length)];

                Brick brick = new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, imgPath);

                bricks.add(brick);
                startGamePane.getChildren().add(brick.getImageView());
            }
        }
        return bricks;
    }

    @FXML
    public Paddle LoadPaddle() {
        double width = 100;
        double height = 20;
        double startX = 550;   // giữa màn hình
        double startY = 600;   // gần đáy
//
//        Image image = new Image(getClass().getResourceAsStream(paddleImages[0]));
//        ImageView imageView = new ImageView(image);
//        imageView.setFitWidth(width);
//        imageView.setFitHeight(height);
//        imageView.setLayoutX(startX);
//        imageView.setLayoutY(startY);
        Paddle paddle = new Paddle(startX, startY, width, height, 10, 0, paddleImages[0]);

        startGamePane.getChildren().add(paddle.getImageView());
        return paddle;
    }

    @FXML
    public Ball LoadBall() {
        double size = 20;

        double startX = 550;   // ngay trên paddle
        double startY = 500;

//        Image image = new Image(getClass().getResourceAsStream(BallImages[0]));
//        ImageView imageView = new ImageView(image);
//        imageView.setFitWidth(size);
//        imageView.setFitHeight(size);
//        imageView.setLayoutX(startX);
//        imageView.setLayoutY(startY);

        Ball ball = new Ball(startX , startY , 20 , 20 , BallImages[0] ,3 ,1 , 1 );
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
}
