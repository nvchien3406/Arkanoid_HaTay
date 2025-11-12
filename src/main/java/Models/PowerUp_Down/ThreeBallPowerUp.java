package Models.PowerUp_Down;

import GameController.GameConstants.GameConstant;
import GameController.Manager.GameManager;
import Models.Ball.Ball;
import Models.Ball.NormalBall;
import Models.Object.GameObject;
import Models.Paddle.Paddle;
import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;
import java.util.List;

public class ThreeBallPowerUp extends PowerUp {

    public ThreeBallPowerUp(double x, double y) {
        super(x, y, 32, 32, GameConstant.powerUpImages[3],
                0, 3, "ThreeBall", 0, false, false, 0);
    }

    @Override
    public void applyEffect(GameObject obj) {
        if (obj instanceof Paddle p) {
            AnchorPane pane = (AnchorPane) GameManager.getInstance().getObjectManager().getPaddle().getImageView().getParent();

            List<Ball> newBalls = createExtraBalls(GameManager.getInstance().getObjectManager().getListBalls());
            addBallsToGame(GameManager.getInstance(), pane, newBalls);
        }
    }

    @Override
    public void removeEffect(GameObject obj) {
        // Không cần xử lý khi hết hiệu lực
    }

    /* -------------------- Helper methods -------------------- */

    /**
     * Tạo thêm 2 bóng (trái & phải) cho mỗi bóng hiện tại
     */
    private List<Ball> createExtraBalls(List<Ball> currentBalls) {
        List<Ball> newBalls = new ArrayList<>();

        for (Ball b : new ArrayList<>(currentBalls)) {
            Ball left = createOffsetBall(b, -0.4);
            Ball right = createOffsetBall(b, 0.4);
            newBalls.add(left);
            newBalls.add(right);
        }

        return newBalls;
    }

    /**
     * Tạo một bóng mới lệch hướng
     */
    private Ball createOffsetBall(Ball baseBall, double offsetX) {
        Ball newBall = new NormalBall(
                baseBall.getX(),
                baseBall.getY(),
                baseBall.getWidth(),
                baseBall.getHeight(),
                GameConstant.BallImages[0],
                4,
                baseBall.getDirectionX() + offsetX,
                baseBall.getDirectionY()
        );
        newBall.setStanding(false);
        return newBall;
    }

    /**
     * Thêm các bóng mới vào GameManager và UI
     */
    private void addBallsToGame(GameManager gm, AnchorPane pane, List<Ball> newBalls) {
        GameManager.getInstance().getObjectManager().getListBalls().addAll(newBalls);
        newBalls.forEach(ball -> pane.getChildren().add(ball.getImageView()));
    }
}
