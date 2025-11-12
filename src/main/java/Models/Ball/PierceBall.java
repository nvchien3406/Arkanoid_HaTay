package Models.Ball;

import GameController.GameConstants.GameConstant;
import GameController.Manager.GameManager;
import GameController.Controllers.StartGameController;
import Models.Brick.Brick;
import Models.Player.Player;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Objects;

public class PierceBall extends Ball {
    private int currentFrame = 0;
    private final long frameDelay = 100_000_000; // 0.1s per frame
    private long lastFrameTime = 0;

    public PierceBall(double x, double y, double speed, double dirX, double dirY) {
        super(x, y,speed, 80, 32, GameConstant.BallImages[1], dirX, dirY);

        Image img = new Image(Objects.requireNonNull(
                getClass().getResource(GameConstant.BallImages[1])
        ).toExternalForm());

        setImageView(new ImageView(img));
        getImageView().setViewport(new Rectangle2D(0, 0, GameConstant.FRAME_WIDTH_PIERCE_BALL, GameConstant.FRAME_HEIGHT_PIERCE_BALL));
        getImageView().setLayoutX(x);
        getImageView().setLayoutY(y);

        setFrameWidth(GameConstant.FRAME_WIDTH_PIERCE_BALL);
        setFrameHeight(GameConstant.FRAME_HEIGHT_PIERCE_BALL);
        setTotalFrames(GameConstant.TOTAL_FRAME_PIERCE_BALL);

        startAnimation();
    }

    @Override
    public void checkWallCollision() {
        GameManager gm = GameManager.getInstance();
        if (getX() <= 0 || getX() + getWidth() >= GameConstant.PANE_WIDTH || getY() <= 0)
            GameManager.getInstance().getObjectManager().markBallForRemoval(this);
    }


    @Override
    protected void onFrameUpdate(long now) {
        double angle = Math.toDegrees(Math.atan2(-directionY, -directionX));
        getImageView().setRotate(angle);
    }

    @Override
    public void playBallMusic() {
        GameManager.getInstance().getSoundService().playExplosion();
    }

    @Override
    public void handleBrickCollision(List<Brick> bricks, Player player, StartGameController controller) {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && checkCollision(brick)) {
                processBrickHit(brick, player, controller);
            }
        }
    }
}
