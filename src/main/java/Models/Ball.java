package Models;


import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;


public class Ball extends MovableObject {
    private double speed, directionX, directionY;

    public Ball () {
        super();
        this.speed = 0;
        this.directionX = 0;
        this.directionY = 0;
    }

    public Ball(double speed, double directionX, double directionY) {
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
    }


    public void bounceOff(GameObject other) {
        final double EPS = 0.5;

        if (!checkCollision(other)) return;

        double ballCenterX = this.getX() + this.getWidth() / 2;
        double ballCenterY = this.getY() + this.getHeight() / 2;

        double otherCenterX = other.getX() + other.getWidth() / 2;
        double otherCenterY = other.getY() + other.getHeight() / 2;

        double dxDistance = ballCenterX - otherCenterX;
        double dyDistance = ballCenterY - otherCenterY;

        double overlapX = (this.getWidth() / 2 + other.getWidth() / 2) - Math.abs(dxDistance);
        double overlapY = (this.getHeight() / 2 + other.getHeight() / 2) - Math.abs(dyDistance);

        if (overlapX < overlapY) {
            // Va chạm theo trục X → đổi hướng X
            directionX *= -1;
            syncVelocity();

            // Đẩy ra khỏi vật để tránh dính
            if (dxDistance > 0) {
                setX(other.getX() + other.getWidth() + EPS);
            } else {
                setX(other.getX() - getWidth() - EPS);
            }
        } else {
            // Va chạm theo trục Y → đổi hướng Y
            directionY *= -1;
            setDy(directionY * speed);

            // Đẩy ra khỏi vật để tránh dính
            if (dyDistance > 0) {
                setY(other.getY() + other.getHeight() + EPS);
            } else {
                setY(other.getY() - getHeight() - EPS);
            }
        }
    }

    public void update() {
        syncVelocity();
        setX(getX() + dx);
        setY(getY() + dy);
    }

    private void syncVelocity() {
        setDx(directionX * speed);
        setDy(directionY * speed);
    }


    public void invertDirX() { this.directionX *= -1; }
    public void invertDirY() { this.directionY *= -1; }

}
