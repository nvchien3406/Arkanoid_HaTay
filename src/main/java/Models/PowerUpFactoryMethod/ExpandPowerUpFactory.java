package Models.PowerUpFactoryMethod;

import Models.PowerUp_Down.ExpandPaddlePowerUp;
import Models.PowerUp_Down.PowerUp;

public class ExpandPowerUpFactory implements PowerUpFactory {
    @Override
    public PowerUp createPowerUp(double x, double y) {
        return new ExpandPaddlePowerUp(x, y);
    }
}
