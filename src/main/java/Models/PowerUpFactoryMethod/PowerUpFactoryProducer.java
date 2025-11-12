package Models.PowerUpFactoryMethod;

import java.util.Random;

public class PowerUpFactoryProducer {

    private static final Random random = new Random();

    public static PowerUpFactory getRandomFactory() {
        double rand = random.nextDouble();

        if (rand < 0.25) {
            return new ExpandPowerUpFactory();
        } else if (rand < 0.5) {
            return new ShrinkPowerDownFactory();
        } else if (rand < 0.75) {
            return new PierceBallPowerUpFactory();
        } else {
            return new ThreeBallPowerUpFactory();
        }
    }
}
