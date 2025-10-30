package Models;

import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public abstract class Brick extends GameObject {
    protected int hitPoints;

    protected String type;

    private final int frameWidth = 32;
    private final int frameHeight = 16;
    private final int totalFrames = 8;
    private int currentFrame = 0;
    private boolean breaking = false;

    private AnimationTimer breakAnimation;

    public Brick() {
        super();
        this.hitPoints = 0;
        this.type = "";
    }

    public Brick(double x, double y, double width, double height, int hitPoints, String type, String path) {
        super(x, y, width, height, path);
        this.hitPoints = hitPoints;
        this.type = type;

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
    }

    public void takeHit() {
        if (breaking || isDestroyed()) return;

        hitPoints--;
        if (hitPoints > 0) {
            update();
        } else {
            startBreakAnimation();
        }
    }

    public boolean isDestroyed() {
        return hitPoints == 0;
    }

    @Override
    public void update() {
        if (currentFrame < totalFrames - 2) {
            currentFrame++;
            imageView.setViewport(new Rectangle2D(currentFrame * frameWidth, 0, frameWidth, frameHeight));
        }
    }

    private void startBreakAnimation() {
        breaking = true;
        currentFrame++;

        breakAnimation = new AnimationTimer() {
            private long lastFrameTime = 0;
            private final long frameDelay = 70_000_000; // 70ms / frame

            @Override
            public void handle(long now) {
                if (now - lastFrameTime < frameDelay) return;
                lastFrameTime = now;

                if (currentFrame < totalFrames) {
                    imageView.setViewport(new Rectangle2D(currentFrame * frameWidth, 0, frameWidth, frameHeight));
                    currentFrame++;
                } else {
                    imageView.setVisible(false);
                    stop();
                }
            }
        };
        breakAnimation.start();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void render(GraphicsContext g) {

    }
}
