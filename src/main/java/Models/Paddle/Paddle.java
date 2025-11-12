package Models.Paddle;

import GameController.Controllers.StartGameController;
import Models.Object.MovableObject;


public class Paddle extends MovableObject {
    private double speed;
    private double baseWidth;
    private boolean moveL = false;
    private boolean moveR = false;

    public Paddle() {
        super();
        speed = 0;
        baseWidth = 0;
        moveL = false;
        moveR = false;
    }

    public Paddle(double x, double y, double width, double height, String path, double dx, double dy, double speed,
                  boolean moveL, boolean moveR) {
        super(x, y, width, height, path, dx, dy);
        this.speed = speed;
        this.moveL = moveL;
        this.moveR = moveR;
        this.imageView.setLayoutX(x);
        this.imageView.setLayoutY(y);
        this.baseWidth = width;
    }

    public void setBaseWidth(double baseWidth) {
        this.baseWidth = baseWidth;
    }

    public boolean isMoveL() {
        return moveL;
    }

    public void setMoveL(boolean moveL) {
        this.moveL = moveL;
    }

    public boolean isMoveR() {
        return moveR;
    }

    public void setMoveR(boolean moveR) {
        this.moveR = moveR;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getBaseWidth() {
        return baseWidth;
    }

    public void moveLeft(StartGameController controller) {
        setX(Math.max(0, getX() - getSpeed()));
        this.setDy(0);
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());
    }

    public void moveRight(StartGameController controller) {
        double maxX = controller.getStartGamePane().getWidth() - getWidth();
        setX(Math.min(maxX, getX() + getSpeed()));
        this.setDy(0);
        imageView.setLayoutX(getX());
        imageView.setLayoutY(getY());
    }

    public void update(StartGameController controller) {
        if (moveL) {
            moveLeft(controller);
        }
        if (moveR) {
            moveRight(controller);
        }
    }
}
