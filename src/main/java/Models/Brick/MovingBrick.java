package Models.Brick;
import GameController.GameConstants.GameConstant;

public class MovingBrick extends BreakableBrick {
    private double startX;
    private double endX;
    private double distance = 70;
    private double speed = 0.8;
    private double direction;

    public MovingBrick(double x, double y, double width, double height, String path, double direction, boolean isLeft) {
        super(x, y, width, height, 3, "MovingBrick", path);
        this.direction = direction;
        if (isLeft) {
            startX = x - distance;
            endX = x;
        } else {
            this.startX = x;
            this.endX = x + distance;
        }

    }

    public void moveBrick() {
        if (imageView == null || isDestroyed()) return;
        x += direction * speed;
        if (x <= startX) {
            x = startX;
            direction *= -1;
        } else if (x >= endX) {
            x = endX;
            direction *= -1;
        }
        imageView.setLayoutX(x);
    }
}
