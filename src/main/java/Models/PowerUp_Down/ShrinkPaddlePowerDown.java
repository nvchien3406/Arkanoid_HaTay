package Models.PowerUp_Down;

import GameController.GameConstant;
import Models.Object.GameObject;
import Models.Paddle.Paddle;
import javafx.scene.image.Image;

import java.util.Objects;


public class ShrinkPaddlePowerDown extends PowerUp{

    public ShrinkPaddlePowerDown(double x, double y) {
        super(x, y, 32, 32, GameConstant.powerUpImages[2], 0, 3,
                "Shrink Paddle", 10.0, false, false, 0.0);
    }

    @Override
    public void applyEffect(GameObject obj) {
        if (obj instanceof Paddle p) {
            double newWidth = p.getBaseWidth() * 0.7;
            double center = p.getX() + p.getWidth() / 2;

            p.setWidth(newWidth);
            p.getImageView().setFitWidth(newWidth);

            double newX = center - newWidth / 2;
            if (newX < 0) newX = 0;
            else if (newX + newWidth > GameConstant.PANE_WIDTH) newX = GameConstant.PANE_WIDTH - newWidth;

            p.setX(newX);
            p.getImageView().setLayoutX(newX);

            p.getImageView().setImage(
                    new Image(Objects.requireNonNull(
                            getClass().getResource(GameConstant.paddleImages)
                    ).toExternalForm())
            );
        }
    }

    @Override
    public void removeEffect(GameObject obj) {
        if (obj instanceof Paddle p) {
            double newWidth = p.getBaseWidth();
            double center = p.getX() + p.getWidth() / 2;

            p.setWidth(newWidth);
            p.getImageView().setFitWidth(newWidth);

            double newX = center - newWidth / 2;
            if (newX < 0) newX = 0;
            else if (newX + newWidth > GameConstant.PANE_WIDTH) newX = GameConstant.PANE_WIDTH - newWidth;

            p.setX(newX);
            p.getImageView().setLayoutX(newX);

            p.getImageView().setImage(
                    new Image(Objects.requireNonNull(
                            getClass().getResource(GameConstant.paddleImages)
                    ).toExternalForm())
            );
        }
    }

}