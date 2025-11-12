package GameController.Manager;

import GameController.Controllers.StartGameController;
import Models.Ball.Ball;
import Models.Ball.NormalBall;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {
    private final ObjectManager objectManager;

    public CollisionManager(ObjectManager objectManager) {
        this.objectManager = objectManager;
    }

    public void checkCollisions(StartGameController controller){
        List<Ball> balls = new ArrayList<>(objectManager.getListBalls()); // lấy từ ObjectManager
        for (Ball ball : balls){
            if (ball instanceof NormalBall) {
                ((NormalBall)ball).controlledBounceOff(objectManager.getPaddle());
            }
            ball.handleBrickCollision(objectManager.getListBricks(), objectManager.getPlayer(), controller);
            ball.checkWallCollision();
        }
    }
}
