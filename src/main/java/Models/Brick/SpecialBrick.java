package Models.Brick;

import javafx.geometry.Rectangle2D;

public class SpecialBrick extends Brick {
    public SpecialBrick() {
        super();
    }

    public SpecialBrick(double x, double y, double width, double height, String path) {
        super(x, y, width, height,100,"SpecialBrick" , path);
        getImageView().setViewport(new Rectangle2D(2 * frameWidth, 0, frameWidth, frameHeight));
    }
}
