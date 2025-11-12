package Models.Object;

import Models.Interface.FrameAnimatable;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;

public abstract class MovableObject extends GameObject implements FrameAnimatable {
    private double speed;
    private double dx,dy;
    private   int frameWidth ;
    private   int frameHeight ;
    private    int totalFrames ;
    private int currentFrame = 0;
    private AnimationTimer animation;

    public MovableObject() {
        super();
        this.dx = 0;
        this.dy = 0;
    }

    public MovableObject(double x, double y, double speed, double width, double height, String path, double dx, double dy) {
        super(x, y, width, height, path);
        this.speed = speed;
        this.dx = dx;
        this.dy = dy;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
    }

    public int getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public AnimationTimer getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationTimer animation) {
        this.animation = animation;
    }

    public void move () {
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }

    @Override
    public void update() {
        move();
    }

    protected void onFrameUpdate(long now) {
    }

    @Override
    public void startAnimation() {
        if (animation != null) return; // tránh tạo lại

        animation = new AnimationTimer() {
            private long lastFrameTime = 0;
            private final long frameDelay = 50_000_000; // 0.2 giây / frame

            @Override
            public void handle(long now) {
                if (now - lastFrameTime < frameDelay) return;
                lastFrameTime = now;

                currentFrame = (currentFrame + 1) % totalFrames;
                getImageView().setViewport(new Rectangle2D(
                        currentFrame * frameWidth, 0, frameWidth, frameHeight
                ));
                onFrameUpdate(now);
            }
        };
        animation.start();
    }

    public void stopAnimation() {
        if (animation != null) animation.stop();
    }
}
