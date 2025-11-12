package Models.Ball;

import GameController.GameConstants.GameConstant;
import GameController.Manager.GameManager;
import GameController.Controllers.StartGameController;
import Models.Brick.Brick;
import Models.Interface.BounceOff;
import Models.Object.GameObject;
import Models.Paddle.Paddle;
import Models.Player.Player;

import java.util.List;

public class NormalBall extends Ball implements BounceOff {
    public NormalBall(double x, double y, double width, double height , String path,
                      double speed, double directionX, double directionY) {
        super(x , y , width , height, path, speed , directionX, directionY);
    }

    public void naturalBounceOff(GameObject other) {
        if (!checkCollision(other)) return;

        GameManager.getInstance().getSoundService().playHit();

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

    public void controlledBounceOff(Paddle paddle) {
        if (dy == 0) return;                // Fix bug bóng đập ngang không đi lên trên
        if (checkCollision(paddle)) {
            GameManager.getInstance().getSoundService().playHit();
            if (directionY > 0 && this.getY() + this.getHeight() <= paddle.getY() + 10){
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

    public void checkWallCollision() {
        if (x <= 0 || x + width >= GameConstant.PANE_WIDTH) {
            GameManager.getInstance().getSoundService().playHit();
            setDirectionX(directionX * -1);
        }
        if (y <= 0) {
            GameManager.getInstance().getSoundService().playHit();
            setDirectionY(directionY * -1);
        }
        if (y + height >= GameConstant.PANE_HEIGHT) {
            GameManager.getInstance().getSoundService().playHit();
            // 🔹 Bóng rơi ra khỏi màn hình -> ẩn ảnh
            if (imageView != null) {
                imageView.setVisible(false);
            }

            // 🔹 Đánh dấu bóng này để GameManager dọn sau khi vòng lặp xong
            GameManager.getInstance().getObjectManager().markBallForRemoval(this);
        }
    }
    public void brickBounceOff(List<Brick> bricks) {
        for (Brick brick : bricks) {
            naturalBounceOff(brick);
        }
    }

    @Override
    public void playBallMusic(){
        GameManager.getInstance().getSoundService().playBreak();
    }

    @Override
    public void handleBrickCollision(List<Brick> bricks, Player player, StartGameController controller) {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && checkCollision(brick)) {
                naturalBounceOff(brick);  // NormalBall bounce
                processBrickHit(brick, player, controller);
                break;
            }
        }
    }
}
