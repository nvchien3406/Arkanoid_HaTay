package Models.PowerUp_Down;

import Models.Object.GameObject;
import Models.Paddle.Paddle;
import javafx.scene.image.Image;
import GameController.GameConstant;

import java.util.Objects;


public class ExpandPaddlePowerUp extends PowerUp {

    public ExpandPaddlePowerUp(double x, double y) {
        super(x, y, 32, 32, "/image/ExpandPaddlePowerUp.png", 0, 3,
                "Expand Paddle", 10.0, false, false, 0.0);
    }

    @Override
    public void applyEffect(GameObject obj) {
        if (obj instanceof Paddle p) {
            double newWidth = p.getBaseWidth() * 1.5;
            double center = p.getX() + p.getWidth() / 2;

            p.setWidth(newWidth);
            p.getImageView().setFitWidth(newWidth);

            double newX = center - newWidth / 2;

            if (newX <= 0) newX = 0;
            else if (newX + newWidth >= GameConstant.PANE_WIDTH) newX = GameConstant.PANE_WIDTH - newWidth;

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

            p.setX(center - newWidth / 2);
            p.getImageView().setLayoutX(p.getX());

            p.getImageView().setImage(
                    new Image(Objects.requireNonNull(
                            getClass().getResource(GameConstant.paddleImages)
                    ).toExternalForm())
            );
        }
    }

}