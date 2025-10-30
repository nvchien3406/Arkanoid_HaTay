package Models;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.List;


public class Ball extends MovableObject {
    private double speed, directionX, directionY;
    private boolean isStanding = true;

    public Ball () {
        super();
        this.speed = 0;
        this.directionX = 0;
        this.directionY = 0;
    }

    public Ball(double x, double y, double width, double height , String path, double speed, double directionX, double directionY) {
        super(x , y , width , height, path);
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
        this.dx = directionX * speed;
        this.dy = directionY * speed;
    }


    public void bounceOff(GameObject other) {
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
            setDx(directionX * speed);

            // Đẩy ra khỏi vật để tránh dính
            if (dxDistance > 0) {
                setX(other.getX() + other.getWidth());
            } else {
                setX(other.getX() - getWidth());
            }
        } else {
            // Va chạm theo trục Y → đổi hướng Y
            directionY *= -1;
            setDy(directionY * speed);

            // Đẩy ra khỏi vật để tránh dính
            if (dyDistance > 0) {
                setY(other.getY() + other.getHeight());
            } else {
                setY(other.getY() - getHeight());
            }
        }
    }

    public void moveBall() {
//        move();
//        imageView.setLayoutX(x);
//        imageView.setLayoutY(y);
        x += directionX * speed;
        y += directionY * speed;
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    public void render(GraphicsContext g) {

    }

    public void checkWallCollision(Paddle paddle) {
        double paneWidth = 1200;
        double paneHeight = 800;

        if (x <= 287 || x + width >= 890 ) {
            setDirectionX(directionX * -1);
        }
        if (y <= 0) {
            setDirectionY(directionY * -1);
        }
        if (y + height >= paneHeight) {
            // rơi xuống -> reset ball lên paddle
            resetBall(paddle);
        }
    }
    public void checkBrickCollision(List<Brick> bricks) {
        for (Brick brick : bricks) {
            if (brick instanceof NormalBrick b && !b.isDestroyed() && checkCollision(brick)) {
                bounceOff(brick);
                brick.takeHit();

                // 🔹 Xác suất sinh PowerUp sau khi brick bị phá
                /*if (Math.random() < 0.2) { // 20% rơi powerup
                    FastBallPowerUp powerUp = new FastBallPowerUp(brick.getX(), brick.getY(), 20, 20, 5.0);
                    GameManager.spawnPowerUp(powerUp);
                }*/

                break;
            }
        }
    }

    //
    public void checkPaddleCollision(Paddle paddle) {
        if (dy == 0) return;
        if (checkCollision(paddle)) {
            if (directionY > 0 && this.getY() + this.getHeight() <= paddle.getY() + 10){
                bounceOff(paddle);

                double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
                double hitPos = (getX() + getWidth() / 2 - paddleCenter) / (paddle.getWidth() / 2);

                setDirectionX(hitPos);
                setDirectionY(-Math.abs(directionY));

                double length = Math.sqrt(directionX * directionX + directionY * directionY);
                setDirectionX(directionX / length);
                setDirectionY(directionY / length);
            }
        }
    }

    public void resetBall(Paddle paddle) {
        x = paddle.getX() + paddle.getWidth() / 2 - width / 2;
        y = paddle.getY() - height;
        directionY = -1;
        directionX = 0.7;
        isStanding = true;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public boolean isStanding() {
        return isStanding;
    }

    public void setStanding(boolean standing) {
        isStanding = standing;
    }

    public void moveBallWithPaddle(Paddle paddle) {
        if (isStanding) {
            x = paddle.getX() + paddle.getWidth() / 2 - width / 2;
            y = paddle.getY() - height;
            imageView.setLayoutX(x);
            imageView.setLayoutY(y);
        } else {
//            directionY = -1;
//            directionX = 0.7;
            moveBall();
        }
    }

}
