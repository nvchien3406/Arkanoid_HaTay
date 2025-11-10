package Models;

public class StrongBrick extends Brick {
    public StrongBrick() {
        super();
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
    public StrongBrick(double x, double y, double width, double height, String path) {
        super(x, y, width, height,2,"StrongBrick" , path);
    }
}
