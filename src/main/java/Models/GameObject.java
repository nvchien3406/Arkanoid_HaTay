package Models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import GameController.GameConstant;

import java.util.Objects;


public abstract class GameObject {
    protected double x, y, width, height;
    protected Image image;
    protected ImageView imageView;

    public GameObject() {
        x = 0;
        y = 0;
        width = 0;
        height = 0;
    }

    public GameObject(double x, double y, double width, double height, String path) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        if (imageView != null) imageView.setLayoutX(x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        if (imageView != null) imageView.setLayoutY(y);
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Image getImage() { return this.image; }

    public void setImage(String path) {
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public abstract void update();

    public boolean checkCollision(GameObject other) {
        double leftA, leftB;
        double rightA, rightB;
        double topA, topB;
        double bottomA, bottomB;

        leftA = this.x;
        rightA = this.x + this.width;
        topA = this.y;
        bottomA = this.y + this.height;

        leftB = other.x;
        rightB = other.x + other.width;
        topB = other.y;
        bottomB = other.y + other.height;

        if (bottomA <= topB) {
            return false;
        }

        if (topA >= bottomB) {
            return false;
        }

        if (rightA <= leftB) {
            return false;
        }

        return !(leftA >= rightB);
    }
}
