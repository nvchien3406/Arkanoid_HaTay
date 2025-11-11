package Models.Object;

public abstract class MovableObject extends GameObject {
    protected double dx,dy;

    public MovableObject() {
        super();
        this.dx = 0;
        this.dy = 0;
    }

    public MovableObject(double x, double y, double width, double height, String path, double dx, double dy) {
        super(x, y, width, height, path);
        this.dx = dx;
        this.dy = dy;
    }

    public MovableObject(double x, double y, double width, double height, String path) {
        super(x, y, width, height, path);
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void move () {
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }

    @Override
    public void update() {
        move();
    }
}
