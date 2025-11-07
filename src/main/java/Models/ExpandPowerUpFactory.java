package Models;

public class ExpandPowerUpFactory implements PowerUpFactory {
    @Override
    public PowerUp createPowerUp(double x, double y) {
        return new ExpandPaddlePowerUp(x, y);
    }
}
