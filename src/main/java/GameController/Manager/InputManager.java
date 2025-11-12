package GameController.Manager;

import Models.AimingArrow.AimingArrow;
import Models.Ball.Ball;
import javafx.scene.Scene;

public class InputManager {
    private final ObjectManager objectManager;
    private final GameUIManager uiManager;

    public InputManager(ObjectManager objectManager, GameUIManager uiManager) {
        this.objectManager = objectManager;
        this.uiManager = uiManager;
    }

    public void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            var paddle = objectManager.getPaddle();
            if (paddle == null) {
                System.err.println("[InputManager] Paddle is null, ignoring key press.");
                return;
            }
            if (event.getCode() == javafx.scene.input.KeyCode.LEFT) objectManager.getPaddle().setMoveL(true);
            if (event.getCode() == javafx.scene.input.KeyCode.RIGHT) objectManager.getPaddle().setMoveR(true);
            if (event.getCode() == javafx.scene.input.KeyCode.SPACE) {
                for (Ball ball : objectManager.getListBalls()) {
                    if (ball.isStanding()) {
                        ball.setStanding(false);
                        ball.setDirectionX((Math.random() < 0.5 ? -(0.4 + Math.random() * 0.6) : 0.4 + Math.random() * 0.6));
                        ball.setDirectionY(-1);
                    }
                }
            }
        });

        scene.setOnKeyReleased(event -> {
            var paddle = objectManager.getPaddle();
            if (paddle == null) {
                System.err.println("[InputManager] Paddle is null, ignoring key press.");
                return;
            }
            if (event.getCode() == javafx.scene.input.KeyCode.LEFT) objectManager.getPaddle().setMoveL(false);
            if (event.getCode() == javafx.scene.input.KeyCode.RIGHT) objectManager.getPaddle().setMoveR(false);
        });

        scene.setOnMousePressed(event -> {
            for (Ball ball : objectManager.getListBalls()) {
                if (ball.isStanding()) {
                    // Lấy arrow ra từ UI Manager
                    AimingArrow arrow = uiManager.getAimingArrow1();

                    // Cập nhật vị trí ban đầu (theo bóng)
                    arrow.followBall(ball);
                    arrow.updateDirection(event.getX(), event.getY(), ball);
                    arrow.show();
                }
            }
        });

        scene.setOnMouseDragged(event -> {
            AimingArrow arrow = uiManager.getAimingArrow1();
            if (arrow.isVisible()) {
                for (Ball ball : objectManager.getListBalls()) {
                    if (ball.isStanding()) {
                        arrow.updateDirection(event.getX(), event.getY(), ball);
                        arrow.followBall(ball); // Đảm bảo di chuyển cùng bóng nếu paddle di chuyển
                    }
                }
            }
        });

        scene.setOnMouseReleased(event -> {
            AimingArrow arrow = uiManager.getAimingArrow1();
            if (arrow.isVisible()) {
                arrow.hide();

                for (Ball ball : objectManager.getListBalls()) {
                    if (ball.isStanding()) {
                        // Tính hướng bắn
                        double centerX = ball.getX() + ball.getWidth() / 2;
                        double centerY = ball.getY() + ball.getHeight() / 2;

                        double deltaX = event.getX() - centerX;
                        double deltaY = event.getY() - centerY;

                        // Ép cho bóng chỉ bay lên
                        if (deltaY >= 0) deltaY = -0.1;
                        if (deltaX == 0) deltaX = 0.01;

                        // Chuẩn hóa vector
                        double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                        double normX = deltaX / magnitude;
                        double normY = deltaY / magnitude;

                        // Gán hướng di chuyển cho bóng
                        ball.setStanding(false);
                        ball.setDirectionX(normX);
                        ball.setDirectionY(normY);
                    }
                }
            }
        });

        // mouse handlers use uiManager/objectManager (similar to your previous code)
        scene.getRoot().requestFocus();
    }

    public void disableKeyControls(Scene scene) {
        scene.setOnKeyPressed(null);
        scene.setOnKeyReleased(null);
        scene.setOnMousePressed(null);
        scene.setOnMouseDragged(null);
        scene.setOnMouseReleased(null);
    }
}
