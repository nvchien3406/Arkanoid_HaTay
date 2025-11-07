package Models;

import GameController.GameManager;
import javafx.scene.image.Image;
import java.util.Objects;

public class PrieceBallPowerUp extends PowerUp {

    public PrieceBallPowerUp() {
        super();
    }

    public PrieceBallPowerUp(double x, double y) {
        super(x, y, 32, 32, "/image/PierceBallPowerUp.png",
                0, 3, "Pierce Ball", 8.0, false, false, 0.0);
    }

    @Override
    public void applyEffect(GameObject obj) {
        // Áp dụng cho Ball
        if (obj instanceof Paddle) {
            // Lấy Ball từ GameManager (vì PowerUp chạm Paddle)
            for (Ball ball : GameManager.getInstance().getListBalls()) {
                ball.setPierceMode(true);

                // Thay ảnh bóng để biểu thị trạng thái xuyên
                ball.getImageView().setImage(
                        new Image(Objects.requireNonNull(
                                getClass().getResource("/image/PierceBallPowerUp.png")
                        ).toExternalForm())
                );
            }
        }
    }

    @Override
    public void removeEffect(GameObject obj) {
        for (Ball ball : GameManager.getInstance().getListBalls()) {
            ball.setPierceMode(false);

            // Trả lại ảnh bóng bình thường
            ball.getImageView().setImage(
                    new Image(Objects.requireNonNull(
                            getClass().getResource("/image/NormalBall.png")
                    ).toExternalForm())
            );
        }
    }
}
