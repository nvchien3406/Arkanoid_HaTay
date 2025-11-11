package Models.PowerUpFactoryMethod;

import Models.PowerUp_Down.PowerUp;

public interface PowerUpFactory {
    PowerUp createPowerUp(double x, double y);
}
