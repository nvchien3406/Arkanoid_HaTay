package Models.Ball;


import GameController.GameConstants.GameConstant;
import GameController.Manager.GameManager;
import GameController.Controllers.StartGameController;
import Models.Brick.*;
import Models.Object.MovableObject;
import Models.Paddle.Paddle;
import Models.Player.Player;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;

import java.util.List;


public abstract class Ball extends MovableObject {
    protected double directionX, directionY;
    protected boolean isStanding = true;


    public Ball() {
        super();

        this.directionX = 0;
        this.directionY = 0;
    }

    public Ball(double x, double y, double speed, double width, double height , String path, double directionX, double directionY) {
        super(x , y , speed, width , height, path, 0 , 0);
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public void moveBall() {
        setX(getX() + directionX * getSpeed());
        setY(getY() + directionY * getSpeed());
        getImageView().setLayoutX(getX());
        getImageView().setLayoutY(getY());
    }

    public abstract void checkWallCollision();
    public abstract void playBallMusic();
    public abstract void handleBrickCollision(List<Brick> bricks , Player player, StartGameController controller);

    public void processBrickHit(Brick brick, Player player, StartGameController controller) {
        if (!(brick instanceof SpecialBrick)) {
            ((BreakableBrick)brick).takeHit();
                player.addScore();
            GameManager.getInstance()
                    .getGameUIManager()
                    .showScorePopup(
                            controller,
                            brick.getX() + brick.getWidth() / 2,
                            brick.getY() + brick.getHeight() / 2,
                            GameConstant.addScore
                    );
            if (brick.isDestroyed()) {
                playBallMusic();
                GameManager.getInstance().getObjectManager().spawnPowerUps(brick, controller);
            }
        }
    }

    public void resetBall(Paddle paddle) {
        setX(paddle.getX() + paddle.getWidth() / 2 - getWidth() / 2);
        setY( paddle.getY() - getHeight());
        directionY = -1;
        directionX = 0.7;
        isStanding = true;
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
            setX(paddle.getX() + paddle.getWidth() / 2 - getWidth() / 2);
            setY(paddle.getY() - getHeight());
            getImageView().setLayoutX(getX());
            getImageView().setLayoutY(getY());
        } else {
            moveBall();
        }
    }


}
