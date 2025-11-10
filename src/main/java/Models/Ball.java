package Models;


import GameController.GameConstant;
import GameController.GameManager;
import GameController.SoundManager;
import GameController.StartGameController;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;


public abstract class Ball extends MovableObject{
    protected double speed, directionX, directionY;
    protected boolean isStanding = true;

    public Ball() {
        super();
        this.speed = 0;
        this.directionX = 0;
        this.directionY = 0;
    }

    public Ball(double speed, double directionX, double directionY, boolean isStanding) {
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
        this.isStanding = isStanding;
    }

    public Ball(double x, double y, double width, double height , String path, double speed, double directionX, double directionY) {
        super(x , y , width , height, path, 0 , 0);
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
        this.dx = directionX * speed;
        this.dy = directionY * speed;
    }

    public void moveBall() {
        x += directionX * speed;
        y += directionY * speed;
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    public abstract void checkWallCollision();
    public abstract void playBallMusic();
    public abstract void handleBrickCollision(List<Brick> bricks , Player player, StartGameController controller);

    protected void processBrickHit(Brick brick, Player player, StartGameController controller) {
        if (!(brick instanceof SpecialBrick)) {
                if (brick instanceof NormalBrick) {
                    ((NormalBrick)brick).takeHit();
                }
                else if (brick instanceof StrongBrick) {
                    ((StrongBrick)brick).takeHit();
                }
                player.addScore();
                GameManager.getInstance().showScorePopup(
                        controller,
                        (brick.getX() + brick.getWidth()) / 2,
                        (brick.getY() + brick.getHeight()) / 2 + brick.getHeight(),
                        GameConstant.addScore
                );
        }

        if (brick.isDestroyed()) playBallMusic();
        GameManager.getInstance().spawnPowerUps(brick, controller);
    }

    public void resetBall(Paddle paddle) {
        x = paddle.getX() + paddle.getWidth() / 2 - width / 2;
        y = paddle.getY() - height;
        directionY = -1;
        directionX = 0.7;
        isStanding = true;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDirectionX() {
        return directionX;
    }

    public double getDirectionY() {
        return directionY;
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
            moveBall();
        }
    }

}
