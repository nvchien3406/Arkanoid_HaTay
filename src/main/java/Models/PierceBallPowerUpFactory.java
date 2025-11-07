package Models;

public class PierceBallPowerUpFactory implements PowerUpFactory {
    @Override
    public PowerUp createPowerUp(double x, double y) {
        return new PierceBallPowerUp(x, y);
    }
}
