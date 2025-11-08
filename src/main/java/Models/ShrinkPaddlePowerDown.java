package Models;

import javafx.scene.image.Image;

import java.util.Objects;


public class ShrinkPaddlePowerDown extends PowerUp implements PaddleVariables {

    public ShrinkPaddlePowerDown() {
        super();
    }

    public ShrinkPaddlePowerDown(double x, double y) {
        super(x, y, 32, 32, "/image/ShrinkPaddlePowerDown.png", 0, 3,
                "Shrink Paddle", 10.0, false, false, 0.0);
    }

    @Override
    public void applyEffect(GameObject obj) {
        if (obj instanceof Paddle) {
            Paddle p = (Paddle) obj;
            double paneWidth = 700;
            double newWidth = p.getBaseWidth() * 0.7;
            double center = p.getX() + p.getWidth() / 2;

            p.setWidth(newWidth);
            p.getImageView().setFitWidth(newWidth);

            double newX = center - newWidth / 2;
            if (newX < 0) newX = 0;
            else if (newX + newWidth > paneWidth) newX = paneWidth - newWidth;

            p.setX(newX);
            p.getImageView().setLayoutX(newX);

            p.getImageView().setImage(
                    new Image(Objects.requireNonNull(
                            getClass().getResource(paddleVariables[0])
                    ).toExternalForm())
            );
        }
    }

    @Override
    public void removeEffect(GameObject obj) {
        if (obj instanceof Paddle) {
            Paddle p = (Paddle) obj;
            double paneWidth = 700;
            double newWidth = p.getBaseWidth();
            double center = p.getX() + p.getWidth() / 2;

            p.setWidth(newWidth);
            p.getImageView().setFitWidth(newWidth);

            double newX = center - newWidth / 2;
            if (newX < 0) newX = 0;
            else if (newX + newWidth > paneWidth) newX = paneWidth - newWidth;

            p.setX(newX);
            p.getImageView().setLayoutX(newX);

            p.getImageView().setImage(
                    new Image(Objects.requireNonNull(
                            getClass().getResource(paddleVariables[0])
                    ).toExternalForm())
            );
        }
    }

}