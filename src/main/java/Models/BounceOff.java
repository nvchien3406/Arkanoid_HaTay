package Models;

import java.util.List;

public interface BounceOff {
    public void naturalBounceOff(GameObject other);
    public void controlledBounceOff(Paddle paddle);
    public void brickBounceOff(List<Brick> brick);
}
