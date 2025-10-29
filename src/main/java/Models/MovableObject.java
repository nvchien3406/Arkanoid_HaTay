package Models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class MovableObject extends GameObject {
    protected double dx,dy;   //velocity

    public MovableObject() {
        super();
        this.dx = 0;
        this.dy = 0;
    }

//    public MovableObject(double dx, double dy) {
//        this.dx = dx;
//        this.dy = dy;
//    }

    public MovableObject(double x, double y, double width, double height, double dx, double dy, String path) {
        super(x, y, width, height, path);
        this.dx = dx;
        this.dy = dy;
    }

    public MovableObject(double x, double y, double width, double height, String path) {
        super(x, y, width, height, path);
        this.dx = dx;
        this.dy = dy;
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

    @Override
    public void update() {
        move();
    }


    public void setX(double x) {
        this.x = x;
        if (imageView != null) imageView.setLayoutX(x);
    }

    public void setY(double y) {
        this.y = y;
        if (imageView != null) imageView.setLayoutY(y);
    }


    public void move () {
        this.setX(this.getX() + dx);
        this.setY(this.getY() + dy);
    }
}
