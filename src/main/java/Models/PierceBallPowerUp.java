package Models;

import GameController.GameManager;
import javafx.scene.layout.AnchorPane;

public class PierceBallPowerUp extends PowerUp {

    public PierceBallPowerUp() {
        super();
    }

    public PierceBallPowerUp(double x, double y) {
        super(x, y, 32, 32,
                "/image/PierceBallPowerUp.png",
                0, 3, "Pierce Ball", 8.0,
                false, false, 0.0);
    }

    @Override
    public void applyEffect(GameObject obj) {
        if (obj instanceof Paddle) {
            GameManager gm = GameManager.getInstance();
            AnchorPane pane = (AnchorPane) gm.getPaddle().getImageView().getParent();

            // Thay toàn bộ bóng hiện có bằng PierceBall
            for (int i = 0; i < gm.getListBalls().size(); i++) {
                Ball oldBall = gm.getListBalls().get(i);

                PierceBall newBall = new PierceBall(
                        oldBall.getX(),
                        oldBall.getY(),
                        oldBall.getSpeed(),
                        oldBall.getDirectionX(),
                        oldBall.getDirectionY()
                );

                newBall.setStanding(oldBall.isStanding());

                // Thay trong danh sách và scene graph
                gm.getListBalls().set(i, newBall);
                pane.getChildren().remove(oldBall.getImageView());
                pane.getChildren().add(newBall.getImageView());
            }

            setActive(true);
        }
    }

    @Override
    public void removeEffect(GameObject obj) {
        GameManager gm = GameManager.getInstance();
        AnchorPane pane = (AnchorPane) gm.getPaddle().getImageView().getParent();

        // Chuyển ngược lại thành Ball thường
        for (int i = 0; i < gm.getListBalls().size(); i++) {
            Ball current = gm.getListBalls().get(i);

            if (current instanceof PierceBall pb) {
                Ball normalBall = new Ball(
                        pb.getX(),
                        pb.getY(),
                        20, 20,
                        "/image/NormalBall.png",
                        pb.getSpeed(),
                        pb.getDirectionX(),
                        pb.getDirectionY()
                );
                normalBall.setStanding(pb.isStanding());

                gm.getListBalls().set(i, normalBall);
                pane.getChildren().remove(pb.getImageView());
                pane.getChildren().add(normalBall.getImageView());
            }
        }
        setActive(false);
    }
}
