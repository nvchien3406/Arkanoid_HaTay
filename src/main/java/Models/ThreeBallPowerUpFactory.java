package Models;

public class ThreeBallPowerUpFactory implements PowerUpFactory {
    @Override
    public PowerUp createPowerUp(double x, double y) {
        return new ThreeBallPowerUp(x, y);
    }
}
