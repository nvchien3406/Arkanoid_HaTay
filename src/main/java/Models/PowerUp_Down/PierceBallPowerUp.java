package Models.PowerUp_Down;

import GameController.Manager.GameManager;
import Models.Ball.Ball;
import Models.Ball.NormalBall;
import Models.Ball.PierceBall;
import Models.Object.GameObject;
import Models.Paddle.Paddle;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class PierceBallPowerUp extends PowerUp{

    public PierceBallPowerUp() {
        super();
    }


    public PierceBallPowerUp(double x, double y) {
        super(x, y, 32, 32,
                "/image/PierceBallPowerUp.png",
                0, 3, "Pierce Ball", 12,
                false, false, 0.0);
    }

    /* -------------------- Helper methods -------------------- */

    private void replaceAllBallsWithPierceBall(GameManager gm, AnchorPane pane, Paddle paddle) {
        removeAllBallsFromPane(gm, pane);

        GameManager.getInstance().getObjectManager().getListBalls().clear();

        PierceBall pierceBall = createPierceBall(paddle);

        GameManager.getInstance().getObjectManager().getListBalls().add(pierceBall);
        addBallToScene(pane, pierceBall);
    }

    private PierceBall createPierceBall(Paddle paddle) {
        PierceBall pierceBall = new PierceBall(
                paddle.getX() + paddle.getWidth() / 2 - 10,
                paddle.getY() - 20,
                4,
                0, -1
        );
        pierceBall.setStanding(true);
        return pierceBall;
    }

    private void restoreNormalBalls(GameManager gm, AnchorPane pane) {
        List<Ball> balls = GameManager.getInstance().getObjectManager().getListBalls();

        for (int i = 0; i < balls.size(); i++) {
            Ball current = balls.get(i);

            if (current instanceof PierceBall pb) {
                Ball normalBall = createNormalBallFromPierce(pb);
                balls.set(i, normalBall);
                replaceBallInPane(pane, pb, normalBall);
            }
        }
    }

    private Ball createNormalBallFromPierce(PierceBall pb) {
        Ball normalBall = new NormalBall(
                pb.getX(),
                pb.getY(),
                20, 20,
                "/image/NormalBall.png",
                pb.getSpeed(),
                pb.getDirectionX(),
                pb.getDirectionY()
        );
        normalBall.setStanding(pb.isStanding());
        return normalBall;
    }

    /* -------------------- UI utility methods -------------------- */

    private void removeAllBallsFromPane(GameManager gm, AnchorPane pane) {
        for (Ball oldBall : new ArrayList<>(GameManager.getInstance().getObjectManager().getListBalls())) {
            pane.getChildren().remove(oldBall.getImageView());
        }
    }

    private void addBallToScene(AnchorPane pane, Ball ball) {
        pane.getChildren().add(ball.getImageView());
    }

    private void replaceBallInPane(AnchorPane pane, Ball oldBall, Ball newBall) {
        pane.getChildren().remove(oldBall.getImageView());
        pane.getChildren().add(newBall.getImageView());
    }

    private void bringPauseMenuToFront(AnchorPane pane) {
        Node pauseMenu = pane.lookup("#pauseMenu");
        if (pauseMenu != null) pauseMenu.toFront();
    }


    @Override
    public void applyEffect(GameObject obj) {
        if (!(obj instanceof Paddle)) return;

        GameManager gm = GameManager.getInstance();
        Paddle paddle = GameManager.getInstance().getObjectManager().getPaddle();
        AnchorPane pane = (AnchorPane) paddle.getImageView().getParent();

        replaceAllBallsWithPierceBall(gm, pane, paddle);
        bringPauseMenuToFront(pane);

        setActive(true);
    }

    @Override
    public void removeEffect(GameObject obj) {
        GameManager gm = GameManager.getInstance();
        AnchorPane pane = (AnchorPane) GameManager.getInstance().getObjectManager().getPaddle().getImageView().getParent();

        restoreNormalBalls(gm, pane);
        setActive(false);
    }
}
