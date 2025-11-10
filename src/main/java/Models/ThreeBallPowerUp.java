package Models;

import GameController.GameManager;
import javafx.scene.layout.AnchorPane;

public class ThreeBallPowerUp extends PowerUp {

    public ThreeBallPowerUp(double x, double y) {
        super(x, y, 32, 32, "/image/ThreeBallsPowerUp.png",
                0, 3, "ThreeBall", 0, false, false, 0);
    }

    @Override
    public void applyEffect(GameObject gameObject) {
        GameManager gm = GameManager.getInstance();
        AnchorPane pane = (AnchorPane) gm.getPaddle().getImageView().getParent();

        // Copy danh sách hiện tại để tránh ConcurrentModificationException
        var currentBalls = new java.util.ArrayList<>(gm.getListBalls());

        for (Ball b : currentBalls) {
            // tạo 2 bóng mới lệch hướng
            Ball left = new NormalBall(b.getX(), b.getY(), b.getWidth(), b.getHeight(),
                    "/image/NormalBall.png", 4, b.getDirectionX() - 0.4, b.getDirectionY());
            Ball right = new NormalBall(b.getX(), b.getY(), b.getWidth(), b.getHeight(),
                    "/image/NormalBall.png", 4, b.getDirectionX() + 0.4, b.getDirectionY());

            left.setStanding(false);
            right.setStanding(false);

            gm.getListBalls().add(left);
            gm.getListBalls().add(right);

            pane.getChildren().addAll(left.getImageView(), right.getImageView());
        }
    }

    @Override
    public void removeEffect(GameObject gameObject) {
        // không cần xóa bóng khi hết hiệu lực
    }
}
