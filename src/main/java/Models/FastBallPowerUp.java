package Models;

import javafx.scene.canvas.GraphicsContext;


public class FastBallPowerUp extends PowerUp {
    public FastBallPowerUp() {
        super();
    }

    public FastBallPowerUp(double x, double y, double width, double height, Double duration) {
        super(x, y, width, height, "FastBallPowerUp", duration);
    }
    public void applyEffect(Paddle paddle){

    }

    public void removeEffect(Paddle paddle){

    }

    @Override
    public void update(){

    }
}
