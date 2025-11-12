package Models.PowerUpFactoryMethod;

import Models.PowerUp_Down.PowerUp;
import Models.PowerUp_Down.ThreeBallPowerUp;

public class ThreeBallPowerUpFactory implements PowerUpFactory {
    @Override
    public PowerUp createPowerUp(double x, double y) {
        return new ThreeBallPowerUp(x, y);
    }
}
