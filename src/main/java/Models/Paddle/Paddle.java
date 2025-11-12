package Models.Paddle;

import GameController.Controllers.StartGameController;
import GameController.GameConstants.GameConstant;
import Models.Object.MovableObject;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;


public class Paddle extends MovableObject {
    private double baseWidth;
    private boolean moveL = false;
    private boolean moveR = false;
    protected final int frameWidth = 80;
    protected final int frameHeight = 16;
    private final int totalFrames = 8;
    private int currentFrame = 0;
    private AnimationTimer animation;

    public Paddle() {
        super();
        baseWidth = 0;
        moveL = false;
        moveR = false;
    }

    public Paddle(double x, double y,double speed , double width, double height, String path, double dx, double dy,
                  boolean moveL, boolean moveR) {
        super(x, y,speed,width, height, path, dx, dy);
        this.moveL = moveL;
        this.moveR = moveR;
        this.baseWidth = width;
        setFrameHeight(GameConstant.FRAME_HEIGHT_PADDLE);
        setFrameWidth(GameConstant.FRAME_WIDTH_PADDLE);
        setTotalFrames(GameConstant.TOTAL_FRAME_PADDLE);
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

    public double getBaseWidth() {
        return baseWidth;
    }

        public void moveLeft(StartGameController controller) {
        setX(Math.max(0, getX() - getSpeed()));
        this.setDy(0);
        getImageView().setLayoutX(getX());
        getImageView().setLayoutY(getY());
    }

    public void moveRight(StartGameController controller) {
        double maxX = controller.getStartGamePane().getWidth() - getWidth();
        setX(Math.min(maxX, getX() + getSpeed()));
        this.setDy(0);
        getImageView().setLayoutX(getX());
        getImageView().setLayoutY(getY());
    }

    public void update(StartGameController controller) {
        if (moveL) {
            moveLeft(controller);
        }
        if (moveR) {
            moveRight(controller);
        }
        startAnimation();
    }

}
