package GameController.Manager;

import GameController.StartGameController;
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
            if (event.getCode() == javafx.scene.input.KeyCode.LEFT) objectManager.getPaddle().setMoveL(false);
            if (event.getCode() == javafx.scene.input.KeyCode.RIGHT) objectManager.getPaddle().setMoveR(false);
        });

        scene.setOnMousePressed(event -> {
            for (Ball ball : objectManager.getListBalls()) {
                // Chỉ ngắm khi bóng đang đứng yên
                if (ball.isStanding()) {
                    // Tính toán tâm quả bóng (giả sử getX/getY là góc trên trái)
                    double ballCenterX = ball.getX() + ball.getWidth() / 2;
                    double ballCenterY = ball.getY() + ball.getHeight() / 2;

                    uiManager.getAimingArrow().setStartX(ballCenterX);
                    uiManager.getAimingArrow().setStartY(ballCenterY);
                    uiManager.updateAimingArrow(event.getX(), event.getY());
                    uiManager.getAimingArrow().setVisible(true);
                }
            }
        });

        scene.setOnMouseDragged(event -> {
            // Chỉ cập nhật khi đang ngắm (mũi tên hiển thị)
            if (uiManager.getAimingArrow().isVisible()) {
                uiManager.updateAimingArrow(event.getX(), event.getY());
            }
        });

        scene.setOnMouseReleased(event -> {
            // Chỉ bắn khi đang ngắm
            if (uiManager.getAimingArrow().isVisible()) {
                for (Ball ball : objectManager.getListBalls()) {
                    uiManager.getAimingArrow().setVisible(false); // Ẩn mũi tên

                    // Tính toán tâm quả bóng
                    double ballCenterX = ball.getX() + ball.getWidth() / 2;
                    double ballCenterY = ball.getY() + ball.getHeight() / 2;

                    // Tính vector hướng
                    double deltaX = event.getX() - ballCenterX;
                    double deltaY = event.getY() - ballCenterY;

                    // Luôn ép bóng bay lên (deltaY phải là số âm)
                    if (deltaY >= 0) {
                        deltaY = -0.1; // Một giá trị nhỏ để tránh lỗi, nếu chỉ click
                    }

                    // Tính độ dài vector (Pythagoras)
                    double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

                    // Chuẩn hóa vector (để có tốc độ không đổi)
                    double normX = deltaX / magnitude;
                    double normY = deltaY / magnitude;

                    // Dựa trên code cũ của bạn, có vẻ setDirectionX/Y là vector hướng
                    ball.setStanding(false);
                    ball.setDirectionX(normX);
                    ball.setDirectionY(normY);
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
