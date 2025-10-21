package GameController;
import Models.Ball;
import Models.Brick;
import Models.NormalBrick;
import Models.Paddle;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartGameController {
    @FXML
    private AnchorPane StartGame;
    //private Button button;

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
    public void LoadBrick(List<Brick> bricks) {
        Random random = new Random();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {

                // Tạo ngẫu nhiên: 20% không có gạch
                //if (random.nextDouble() < 0.2) continue;

                double x = col * BRICK_WIDTH + 300;
                double y = row * BRICK_HEIGHT + 50;

                String imgPath = brickImages[random.nextInt(brickImages.length)];
                Image image = new Image(getClass().getResourceAsStream(imgPath));

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(BRICK_WIDTH);
                imageView.setFitHeight(BRICK_HEIGHT);
                imageView.setLayoutX(x);
                imageView.setLayoutY(y);

                imageView.setViewport(new Rectangle2D(0, 0, BRICK_WIDTH, BRICK_HEIGHT));

                Brick brick = new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, image, imageView);

                bricks.add(brick);
                StartGame.getChildren().add(imageView);
            }
        }
    }

    @FXML
    public void LoadPaddle(Paddle paddle) {
        double width = 100;
        double height = 20;
        double startX = 550;   // giữa màn hình
        double startY = 600;   // gần đáy

        Image image = new Image(getClass().getResourceAsStream(paddleImages[0]));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(startX);
        imageView.setLayoutY(startY);

        paddle = new Paddle(startX, startY, width, height, 10, 0, image, imageView);

        StartGame.getChildren().add(imageView);
    }

    @FXML
    public void LoadBall(Ball ball) {
        double size = 20;

        double startX = 550;   // ngay trên paddle
        double startY = 500;

        Image image = new Image(getClass().getResourceAsStream(BallImages[0]));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setLayoutX(startX);
        imageView.setLayoutY(startY);

        ball = new Ball(startX , startY , 20 , 20 , image , imageView ,15 ,1 , 1 );
        StartGame.getChildren().add(imageView);
    }
}
