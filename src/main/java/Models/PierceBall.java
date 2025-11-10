package Models;

import GameController.GameConstant;
import GameController.GameManager;
import GameController.SoundManager;
import GameController.StartGameController;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Objects;

public class PierceBall extends Ball {
    private int currentFrame = 0;
    private final int totalFrames = 4;
    private final int frameWidth = 80;
    private final int frameHeight = 32;
    private final long frameDelay = 100_000_000; // 0.1s per frame
    private long lastFrameTime = 0;
    private AnimationTimer animationTimer;

    public PierceBall(double x, double y, double speed, double dirX, double dirY) {
        super(x, y, 80, 32, "/image/PierceBallPowerUp1.png", speed, dirX, dirY);

        Image img = new Image(Objects.requireNonNull(
                getClass().getResource("/image/PierceBallPowerUp1.png")
        ).toExternalForm());

        imageView = new ImageView(img);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        startAnimation();
    }

    @Override
    public void checkWallCollision() {
        GameManager gm = GameManager.getInstance();
        if (x <= 0 || x + width >= GameConstant.PANE_WIDTH || y <= 0)
            GameManager.getInstance().markBallForRemoval(this);
    }


    private void startAnimation() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // === Frame animation ===
                if (now - lastFrameTime > frameDelay) {
                    lastFrameTime = now;
                    currentFrame = (currentFrame + 1) % totalFrames;
                    imageView.setViewport(new Rectangle2D(
                            currentFrame * frameWidth, 0, frameWidth, frameHeight
                    ));
                }

                // === Rotation based on direction vector ===
                double angle = Math.toDegrees(Math.atan2(-getDirectionY(), -getDirectionX()));
                imageView.setRotate(angle);
            }
        };
        animationTimer.start();
    }

    public void stopAnimation() {
        if (animationTimer != null) animationTimer.stop();
    }

    @Override
    public void playBallMusic() {
        SoundManager.PlayExplosion();
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
