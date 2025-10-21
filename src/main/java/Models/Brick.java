package Models;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;



public class Brick extends GameObject {
    protected int hitPoints, maxHitPoints;
    protected ImageView  imageView;
    protected double frameWidth, frameHeight;
    protected String type;

    public Brick() {
        super();
        this.hitPoints = 0;
        this.type = "";
    }

    public Brick(double x, double y, double width, double height, int hitPoints, String type, String path) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.maxHitPoints = hitPoints;
        this.frameWidth = width;
        this.frameHeight = height;

        Image sprite = new Image(getClass().getResourceAsStream(path));
        imageView = new ImageView(sprite);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        updateFrame();
    }

    public void takeHit() {
        if (hitPoints > 0) {
            hitPoints--;
            updateFrame();
        }
    }

    public boolean isDestroyed() {
        return hitPoints == 0;
    }

    public void updateFrame() {
        int currentFrame = maxHitPoints - hitPoints;
        double xViewport = currentFrame * frameWidth;
        imageView.setViewport(new Rectangle2D(xViewport, 0, frameWidth, frameHeight));

        // Khi hết máu thì ẩn luôn viên gạch
        if (hitPoints <= 0) {
            imageView.setVisible(false);
        }
    }

    @Override
    public void update() {

    }

    public void render(GraphicsContext g) {

    }

    public ImageView getImageView() {
        return imageView;
    }
}
