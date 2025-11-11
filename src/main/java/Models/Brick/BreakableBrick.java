package Models.Brick;

import Models.Interface.CanTakeHit;
import javafx.geometry.Rectangle2D;

public abstract class BreakableBrick extends Brick implements CanTakeHit {
    public BreakableBrick(double x, double y, double width, double height, int hitPoints, String type, String path) {
        super(x, y, width, height,hitPoints, type, path);
    }

    public void takeHit() {
        if (breaking || isDestroyed() ) return;

        hitPoints--;
        if (hitPoints > 0) {
            update();
        } else {
            startBreakAnimation();

        }
    }

}
