package Models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class Paddle extends MovableObject{
    private double speed, currentPowerUp;

    public double getSpeed() {
        return speed;
    }

    public Paddle() {
        super();
        speed = 0;
        currentPowerUp = 0;
    }

    public Paddle(double x, double y, double width, double height, double speed, double currentPowerUp, String path) {
        super(x, y, width, height, 0, 0, path);
        this.speed = speed;
        this.currentPowerUp = currentPowerUp;
        this.imageView.setLayoutX(x);
        this.imageView.setLayoutY(y);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getCurrentPowerUp() {
        return currentPowerUp;
    }

    public void setCurrentPowerUp(double currentPowerUp) {
        this.currentPowerUp = currentPowerUp;
    }

    public void moveLeft() {
        this.setDx(-speed);
        this.setDy(0);
    }

    public void moveRight() {
        this.setDx(speed);
        this.setDy(0);
    }

    public void applyPowerUp() {
        if (currentPowerUp == 1) {
            // PowerUp loại 1: tăng tốc
            speed *= 1.5;
        } else if (currentPowerUp == 2) {
            // PowerUp loại 2: mở rộng chiều rộng thanh đỡ
            setWidth(getWidth() * 1.2);
        }
    }

    public void render(GraphicsContext g) {

    }


}
