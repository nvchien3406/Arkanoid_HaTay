package Models.PowerUpFactoryMethod;

import Models.PowerUp_Down.PowerUp;
import Models.PowerUp_Down.ShrinkPaddlePowerDown;

public class ShrinkPowerDownFactory implements PowerUpFactory {
    @Override
    public PowerUp createPowerUp(double x, double y) {
        return new ShrinkPaddlePowerDown(x, y);
    }
}
