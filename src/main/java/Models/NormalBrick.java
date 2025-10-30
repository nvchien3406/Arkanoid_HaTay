package Models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NormalBrick extends Brick {

    public NormalBrick() {
        super();
    }

    public NormalBrick(double x, double y, double width, double height, String path) {
        super(x, y, width, height,1,"NormalBrick" , path);
    }
}
