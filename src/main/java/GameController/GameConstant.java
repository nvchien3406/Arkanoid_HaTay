package GameController;

import javafx.util.Pair;

public interface GameConstant {
    int ROWS = 14;
    int COLS = 18;
    int BRICK_WIDTH = 32;
    int BRICK_HEIGHT = 16;

    int PANE_WIDTH = 700;
    int PANE_HEIGHT = 700;

    double PADDLE_WIDTH = 100;
    double PADDLE_HEIGHT = 20;
    double PADDLE_SPEED = 3;

    double BALL_SIZE = 20;
    double BALL_SPEED = 3;

    Pair<String, String>[] brickImages = new Pair[]{
            new Pair<>("/image/BlueBrick.png", "NormalBrick"),
            new Pair<>("/image/GreenBrick.png", "NormalBrick"),
            new Pair<>("/image/OrangeBrick.png", "NormalBrick"),
            new Pair<>("/image/PurpleBrick.png", "StrongBrick"),
            new Pair<>("/image/RedBrick.png", "StrongBrick"),
            new Pair<>("/image/YellowBrick.png", "StrongBrick"),
            new Pair<>("/image/SpecialBrick.png", "SpecialBrick")
    };
    String[] paddleImages = {
            "/image/Paddle.png"
    };
    String[] BallImages = {
            "/image/Ball.png"
    };
    String[] powerUpImages = {
            "/image/ExpandPaddlePowerUp.png"
    };
}
