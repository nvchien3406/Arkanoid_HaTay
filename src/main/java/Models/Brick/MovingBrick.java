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
        if (getImageView() == null || isDestroyed()) return;
        setX( getX() + direction * speed);
        if (getX() <= startX) {
            setX(startX);
            direction *= -1;
        } else if (getX() >= endX) {
            setX(endX);
            direction *= -1;
        }
        getImageView().setLayoutX(getX());
    }
}
