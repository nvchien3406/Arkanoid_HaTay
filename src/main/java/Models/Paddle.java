package Models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class Paddle extends MovableObject{
    private double speed;
    public boolean moveL = false;
    public boolean moveR = false;
    private double baseWidth;

    public double getSpeed() {
        return speed;
    }

    public Paddle() {
        super();
        speed = 0;
    }

    public Paddle(double x, double y, double width, double height, String path, double dx, double dy, double speed,
                  boolean moveL, boolean moveR) {
        super(x, y, width, height, path, dx, dy);
        this.speed = speed;
        this.moveL = moveL;
        this.moveR = moveR;
        this.imageView.setLayoutX(x);
        this.imageView.setLayoutY(y);
        this.baseWidth = width;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getBaseWidth() {
        return baseWidth;
    }

    public void moveLeft() {
        setX(Math.max(0, getX() - getSpeed()));
        this.setDy(0);
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());
    }

    public void moveRight() {
        double maxX = 1200 - getWidth();
        setX(Math.min(maxX, getX() + getSpeed()));
        this.setDy(0);
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());
    }

    public void movePaddle() {
        double paneWidth = 1200;
        double leftLimit  = 0.25 * paneWidth;
        double rightLimit = 890;

        if (moveL && x > leftLimit) {
            moveLeft();
        }
        if (moveR && x + width + 15.5 < rightLimit) {
            moveRight();
        }
    }
}
