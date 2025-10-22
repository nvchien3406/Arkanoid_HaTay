package View;

import Models.Brick;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
    private final GraphicsContext gc;

    public Renderer(GraphicsContext gc) { this.gc = gc; }

    public void drawBrick(Brick b) {
        int fi = b.getFrameIndex();
        double sx = fi * b.getFrameWidth();
        gc.drawImage(
                b.getImage(),
                sx, 0, b.getFrameWidth(), b.getFrameHeight(),   // source rect
                b.getX(), b.getY(), b.getWidth(), b.getHeight() // dest rect
        );
    }
}
