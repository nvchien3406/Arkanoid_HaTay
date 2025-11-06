package Models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Objects;


public class ExpandPaddlePowerUp extends PowerUp implements PaddleVariables {

    public ExpandPaddlePowerUp() {
        super();
    }

    public ExpandPaddlePowerUp(double x, double y) {
        super(x, y, 16, 16, "/image/ExpandPaddlePowerUp.png", 0, 5,
                "Expand Paddle", 3.0, false, false, 0.0);
    }

    public void moveExpandPaddlePowerUp() {
        y += 0.5;
        imageView.setLayoutY(y);
    }

    @Override
    public void applyEffect(GameObject obj) {
        if (obj instanceof Paddle) {
            Paddle p = (Paddle) obj;

            double newWidth = p.getBaseWidth() * 1.5;
            double center   = p.getX() + p.getWidth() / 2;

            p.setWidth(newWidth);
            p.getImageView().setFitWidth(newWidth);

            p.setX(center - newWidth / 2);
            p.getImageView().setLayoutX(p.getX());

            p.getImageView().setImage(
                    new Image(Objects.requireNonNull(
                            getClass().getResource(paddleVariables[1])
                    ).toExternalForm())
            );
        }
    }

    @Override
    public void removeEffect(GameObject obj) {
        if (obj instanceof Paddle) {
            Paddle p = (Paddle) obj;

            double newWidth = p.getBaseWidth();
            double center = p.getX() + p.getWidth() / 2;

            p.setWidth(newWidth);
            p.getImageView().setFitWidth(newWidth);

            p.setX(center - newWidth / 2);
            p.getImageView().setLayoutX(p.getX());

            p.getImageView().setImage(
                    new Image(Objects.requireNonNull(
                            getClass().getResource(paddleVariables[0])
                    ).toExternalForm())
            );
        }
    }

}