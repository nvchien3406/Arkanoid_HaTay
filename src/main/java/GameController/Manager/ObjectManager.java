package GameController.Manager;

import GameController.GameConstants.GameConstant;
import GameController.Controllers.StartGameController;
import Models.Ball.Ball;
import Models.Ball.NormalBall;
import Models.Ball.PierceBall;
import Models.Brick.Brick;
import Models.Brick.MovingBrick;
import Models.Paddle.Paddle;
import Models.Player.Player;
import Models.PowerUpFactoryMethod.PowerUpFactory;
import Models.PowerUpFactoryMethod.PowerUpFactoryProducer;
import Models.PowerUp_Down.PowerUp;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.List;

public class ObjectManager {

    private Paddle paddle;
    private List<Ball> listBalls = new ArrayList<>();
    private List<Brick> listBricks = new ArrayList<>();
    private List<PowerUp> listPowerUps = new ArrayList<>();
    private Player player;
    private Line aimingArrow;

    // === Deferred removal lists ===
    private final List<Ball> ballsToRemove = new ArrayList<>();
    private final List<PowerUp> powerUpsToRemove = new ArrayList<>();
    private final List<PowerUp> powerUpsToAdd = new ArrayList<>();

    /* ---------------- GETTER / SETTER ---------------- */
    public Paddle getPaddle() { return paddle; }
    public void setPaddle(Paddle paddle) { this.paddle = paddle; }

    public List<Ball> getListBalls() { return listBalls; }
    public void setListBalls(List<Ball> listBalls) { this.listBalls = listBalls; }

    public List<Brick> getListBricks() { return listBricks; }
    public void setListBricks(List<Brick> listBricks) { this.listBricks = listBricks; }

    public List<PowerUp> getListPowerUps() { return listPowerUps; }
    public void setListPowerUps(List<PowerUp> listPowerUps) { this.listPowerUps = listPowerUps; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public Line getAimingArrow() { return aimingArrow; }
    public void setAimingArrow(Line aimingArrow) { this.aimingArrow = aimingArrow; }

    /* ---------------- CLEAR & MARK ---------------- */
    public void clearCollections() {
        ballsToRemove.clear();
        powerUpsToRemove.clear();
        powerUpsToAdd.clear();
    }

    public void markBallForRemoval(Ball b) {
        if (b != null && !ballsToRemove.contains(b)) ballsToRemove.add(b);
    }

    public void markPowerUpForRemoval(PowerUp p) {
        if (p != null && !powerUpsToRemove.contains(p)) powerUpsToRemove.add(p);
    }

    public void queuePowerUpToAdd(PowerUp p) {
        if (p != null) powerUpsToAdd.add(p);
    }

    public void removePowerUp(PowerUp powerUp) {
        if (listPowerUps != null && listPowerUps.remove(powerUp)) {
            if (powerUp.getImageView() != null) powerUp.getImageView().setVisible(false);
        }
    }

    /* ---------------- UPDATE ---------------- */
    public void updateBall() {
        for (Ball ball : new ArrayList<>(listBalls)) {
            ball.moveBallWithPaddle(paddle);
        }
    }

    public void updatePaddle(StartGameController controller) {
        if (paddle != null) paddle.update(controller);
    }

    public void updatePlayer(StartGameController controller) {
        if (player != null) controller.updateCurrentScore(player.getScore());
    }

    public void updatePowerUps() {
        if (listPowerUps.isEmpty()) return;
        for (PowerUp p : new ArrayList<>(listPowerUps)) {
            p.update(paddle);
            p.checkPaddleCollision(paddle);
            if (p.isExpired()) markPowerUpForRemoval(p);
        }
    }

    public void updateBricks() {
        for (Brick brick : listBricks) {
            if (brick instanceof MovingBrick movingBrick) {
                movingBrick.moveBrick();
            }
        }
    }

    /* ---------------- CLEANUP ---------------- */
    public void cleanupDeferred(StartGameController controller) {
        // Add queued PowerUps
        if (!powerUpsToAdd.isEmpty()) {
            listPowerUps.addAll(powerUpsToAdd);
            powerUpsToAdd.clear();
        }

        // Remove expired PowerUps
        if (!powerUpsToRemove.isEmpty()) {
            for (PowerUp p : powerUpsToRemove) {
                listPowerUps.remove(p);
                if (p.getImageView() != null) p.getImageView().setVisible(false);
            }
            powerUpsToRemove.clear();
        }

        // Remove deferred Balls
        if (!ballsToRemove.isEmpty()) {
            boolean pierceBallRemoved = false;
            for (Ball b : ballsToRemove) {
                if (b instanceof PierceBall) pierceBallRemoved = true;
                listBalls.remove(b);
                if (b.getImageView() != null) b.getImageView().setVisible(false);
            }
            ballsToRemove.clear();

            if (listBalls.isEmpty()) {
                if (pierceBallRemoved)
                    spawnBallOnPaddleWithoutLosingLife(controller);
                else
                    spawnBallOnPaddleAndLoseLife(controller);
            }
        }
    }

    /* ---------------- BALL SPAWN HELPERS ---------------- */
    private void spawnBallOnPaddleAndLoseLife(StartGameController controller) {
        if (paddle == null || player == null) return;

        Ball newBall = new NormalBall(
                paddle.getX() + paddle.getWidth() / 2 - 10,
                paddle.getY() - 20,
                20, 20,
                GameConstant.BallImages[0],
                3, 0, -1
        );
        newBall.setStanding(true);
        listBalls.add(newBall);
        controller.getStartGamePane().getChildren().add(newBall.getImageView());
        Node pauseMenu = controller.getStartGamePane().lookup("#pauseMenu");
        if (pauseMenu != null) pauseMenu.toFront();
        player.setLives(player.getLives() - 1);
    }

    private void spawnBallOnPaddleWithoutLosingLife(StartGameController controller) {
        if (paddle == null) return;
        Ball newBall = new NormalBall(
                paddle.getX() + paddle.getWidth() / 2 - 10,
                paddle.getY() - 20,
                20, 20,
                GameConstant.BallImages[0],
                3, 0, -1
        );
        newBall.setStanding(true);
        listBalls.add(newBall);
        controller.getStartGamePane().getChildren().add(newBall.getImageView());
        Node pauseMenu = controller.getStartGamePane().lookup("#pauseMenu");
        if (pauseMenu != null) pauseMenu.toFront();
    }

    /* ---------------- POWERUP SPAWN ---------------- */
    public boolean hasActivePowerUp() {
        for (PowerUp p : listPowerUps)
            if (p.isActive() && !p.isExpired()) return true;
        return false;
    }

    public void spawnPowerUps(Brick brick, StartGameController controller) {
        if (listBalls.size() == 1 && listPowerUps.stream().noneMatch(p -> !p.isExpired()) && !hasActivePowerUp()) {
            PowerUpFactory factory = PowerUpFactoryProducer.getRandomFactory();
            PowerUp powerUp = factory.createPowerUp(brick.getX() + 10, brick.getY());
            listPowerUps.add(powerUp);
            controller.getStartGamePane().getChildren().add(powerUp.getImageView());
            Node pauseMenu = controller.getStartGamePane().lookup("#pauseMenu");
            if (pauseMenu != null) pauseMenu.toFront();
        }
    }

    /* ---------------- LEVEL CLEANUP ---------------- */
    public void clearLevelObjects(StartGameController controller) {
        if (controller == null) return;
        clearCollections();
        if (listBricks != null) listBricks.clear();
        if (paddle != null && paddle.getImageView() != null) {
            controller.getStartGamePane().getChildren().remove(paddle.getImageView());
        }
        paddle = null;

        for (Ball b : listBalls) {
            controller.getStartGamePane().getChildren().remove(b.getImageView());
        }
        listBalls.clear();
        for (PowerUp p : listPowerUps) {
            controller.getStartGamePane().getChildren().remove(p.getImageView());
        }
        listPowerUps.clear();
    }
}
