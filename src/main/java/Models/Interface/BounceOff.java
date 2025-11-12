package Models.Interface;

import Models.Brick.Brick;
import Models.Object.GameObject;
import Models.Paddle.Paddle;

import java.util.List;

public interface BounceOff {
    public void naturalBounceOff(GameObject other);
    public void controlledBounceOff(Paddle paddle);
    public void brickBounceOff(List<Brick> brick);
}
