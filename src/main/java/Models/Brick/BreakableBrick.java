package Models.Brick;

import Models.Interface.CanTakeHit;
import Models.Interface.FrameAnimatable;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;

public abstract class BreakableBrick extends Brick implements CanTakeHit , FrameAnimatable {
    protected final int frameWidth = 32;
    protected final int frameHeight = 16;
    protected final int totalFrames = 8;
    protected int currentFrame = 0;
    protected boolean breaking = false;

    public BreakableBrick(double x, double y, double width, double height, int hitPoints, String type, String path) {
        super(x, y, width, height,hitPoints, type, path);
    }

    public void takeHit() {
        if (isBreaking() || isDestroyed() ) return;

        hitPoints--;
        if (hitPoints > 0) {
            update();
        } else {
            setBreaking(true);
            startAnimation();

        }
    }


    @Override
    public void startAnimation() {
        currentFrame++;

        // 70ms / frame
        AnimationTimer breakAnimation = new AnimationTimer() {
            private long lastFrameTime = 0;
            private final long frameDelay = 50_000_000; // 70ms / frame

            @Override
            public void handle(long now) {
                // 70ms / frame
                long frameDelay = 70_000_000;
                if (now - lastFrameTime < frameDelay) return;
                lastFrameTime = now;

                if (currentFrame < totalFrames) {
                    getImageView().setViewport(new Rectangle2D(currentFrame * frameWidth, 0, frameWidth, frameHeight));
                    currentFrame++;
                } else {
                    getImageView().setVisible(false);
                    stop();
                }
            }
        };
        breakAnimation.start();
    }

}
