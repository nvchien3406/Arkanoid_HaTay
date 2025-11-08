package Models;

public class ShrinkPowerDownFactory implements PowerUpFactory {
    @Override
    public PowerUp createPowerUp(double x, double y) {
        return new ShrinkPaddlePowerDown(x, y);
    }
}
