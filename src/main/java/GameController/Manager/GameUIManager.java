package GameController.Manager;

import GameController.Controllers.StartGameController;
import GameController.GameConstants.GameConstant;
import Models.AimingArrow.AimingArrow;
import javafx.animation.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GameUIManager {
    private AimingArrow aimingArrow;

    public AimingArrow getAimingArrow() {
        return aimingArrow;
    }

    public void setAimingArrow(AimingArrow aimingArrow) {
        this.aimingArrow = aimingArrow;
    }

    public void showScorePopup(StartGameController controller , double x, double y, int score) {
        Text scoreText = new Text("+" + score);
        scoreText.setFill(Color.GOLD);
        scoreText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        scoreText.setLayoutX(x);
        scoreText.setLayoutY(y);

        controller.getStartGamePane().getChildren().add(scoreText);

        ScaleTransition scale = new ScaleTransition(Duration.millis(400), scoreText);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.5);
        scale.setToY(1.5);

        FadeTransition fade = new FadeTransition(Duration.millis(800), scoreText);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        TranslateTransition moveUp = new TranslateTransition(Duration.millis(800), scoreText);
        moveUp.setByY(-40);

        ParallelTransition anim = new ParallelTransition(scale, fade, moveUp);
        anim.setOnFinished(e -> controller.getStartGamePane().getChildren().remove(scoreText));
        anim.play();
    }

    public void showLevelIntro(StartGameController controller, int levelNumber) {
        AnchorPane pane = controller.getStartGamePane();
        if (pane == null) return;

        // 1️⃣ Tạo text
        Text levelText = new Text("LEVEL " + levelNumber);
        levelText.setFill(Color.GOLD);
        levelText.setStroke(Color.BLACK);
        levelText.setStrokeWidth(3);

        levelText.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/Bangers-Regular.ttf"), 72));
        pane.getChildren().add(levelText);

        Runnable playAnimation = () -> {
            double centerX = (pane.getWidth() - levelText.getLayoutBounds().getWidth()) / 2;
            double centerY = (pane.getHeight() / 2);
            levelText.setLayoutX(centerX);
            levelText.setLayoutY(centerY);

            ScaleTransition scale = new ScaleTransition(Duration.millis(700), levelText);
            scale.setFromX(0.5);
            scale.setFromY(0.5);
            scale.setToX(1.2);
            scale.setToY(1.2);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(400), levelText);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(800), levelText);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setDelay(Duration.millis(1000));

            ParallelTransition appear = new ParallelTransition(scale, fadeIn);
            SequentialTransition totalAnim = new SequentialTransition(appear, fadeOut);
            totalAnim.setOnFinished(e -> pane.getChildren().remove(levelText));
            totalAnim.play();
        };

        // 🟡 Nếu pane chưa layout xong -> đợi khi nó hiển thị trên màn hình
        if (pane.getWidth() == 0 || pane.getHeight() == 0) {
            pane.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.getWidth() > 0 && newVal.getHeight() > 0) {
                    // Chỉ chạy 1 lần
                    pane.layoutBoundsProperty().removeListener((o, ov, nv) -> {});
                    playAnimation.run();
                }
            });
        } else {
            playAnimation.run();
        }
    }
}
