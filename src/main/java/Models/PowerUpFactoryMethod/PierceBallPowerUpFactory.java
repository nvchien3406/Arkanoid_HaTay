package Models.PowerUpFactoryMethod;

import Models.PowerUp_Down.PierceBallPowerUp;
import Models.PowerUp_Down.PowerUp;

public class PierceBallPowerUpFactory implements PowerUpFactory {
    @Override
    public PowerUp createPowerUp(double x, double y) {
        return new PierceBallPowerUp(x, y);
    }
}
