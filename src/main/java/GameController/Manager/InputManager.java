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
