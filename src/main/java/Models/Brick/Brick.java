package Models.Brick;

import Models.Object.GameObject;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;


public abstract class Brick extends GameObject {
    protected int hitPoints;

    protected String type;

    protected final int frameWidth = 32;
    protected final int frameHeight = 16;
    protected final int totalFrames = 8;
    protected int currentFrame = 0;
    protected boolean breaking = false;

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

        getImageView().setFitWidth(width);
        getImageView().setFitHeight(height);
        getImageView().setLayoutX(x);
        getImageView().setLayoutY(y);
        getImageView().setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));
    }

    public boolean isDestroyed() {
        return hitPoints == 0;
    }

    @Override
    public void update() {
        if (currentFrame < totalFrames - 2) {
            currentFrame++;
            getImageView().setViewport(new Rectangle2D(currentFrame * frameWidth, 0, frameWidth, frameHeight));
        }
    }

    public boolean isBreaking() {
        return breaking;
    }

    public void setBreaking(boolean breaking) {
        this.breaking = breaking;
    }

    public AnimationTimer getBreakAnimation() {
        return breakAnimation;
    }

    public void setBreakAnimation(AnimationTimer breakAnimation) {
        this.breakAnimation = breakAnimation;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }
}
