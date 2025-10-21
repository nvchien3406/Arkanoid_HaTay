package Models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public abstract class Brick extends GameObject {
    protected int hitPoints;

    protected String type;

    public Brick() {
        super();
        this.hitPoints = 0;
        this.type = "";
    }

    public Brick(double x, double y, double width, double height, int hitPoints, String type , Image image , ImageView imageView) {
        super(x, y, width, height , image , imageView);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    public void takeHit() {
        hitPoints--;
    }

    public boolean isDestroyed() {
        return hitPoints == 0;
    }

    @Override
    public void update() {

    }

    public void render(GraphicsContext g) {

    }
}
