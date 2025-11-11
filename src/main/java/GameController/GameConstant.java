package GameController;

import javafx.util.Pair;

public final class GameConstant {
    public static final int ROWS = 14;
    public static final int COLS = 18;
    public static final int BRICK_WIDTH = 32;
    public static final int BRICK_HEIGHT = 16;

    public static final int PANE_WIDTH = 700;
    public static final int PANE_HEIGHT = 700;

    public static final double PADDLE_WIDTH = 100;
    public static final double PADDLE_HEIGHT = 20;
    public static final double PADDLE_SPEED = 3;

    public static final double BALL_SIZE = 20;
    public static final double BALL_SPEED = 3;
    public static final int addScore = 10;

    public static final Pair<String, String>[] brickImages = new Pair[]{
            new Pair<>("/image/BlueBrick.png", "NormalBrick"),
            new Pair<>("/image/GreenBrick.png", "NormalBrick"),
            new Pair<>("/image/OrangeBrick.png", "NormalBrick"),
            new Pair<>("/image/PurpleBrick.png", "StrongBrick"),
            new Pair<>("/image/RedBrick.png", "StrongBrick"),
            new Pair<>("/image/YellowBrick.png", "StrongBrick"),
            new Pair<>("/image/SpecialBrick.png", "SpecialBrick")
    };
    public static final String paddleImages = "/image/Paddle.png";

    public static final String[] BallImages = {
            "/image/NormalBall.png",
            "/image/PierceBall.png"
    };
    public static final String[] powerUpImages = {
            "/image/ExpandPaddlePowerUp.png"
    };
}
