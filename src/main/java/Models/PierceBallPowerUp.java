package Models;

import GameController.GameConstant;
import GameController.GameManager;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class PierceBallPowerUp extends PowerUp implements GameConstant {

    public PierceBallPowerUp() {
        super();
    }


    public PierceBallPowerUp(double x, double y) {
        super(x, y, 32, 32,
                "/image/PierceBallPowerUp.png",
                0, 3, "Pierce Ball", 12,
                false, false, 0.0);
    }

    @Override
    public void applyEffect(GameObject obj) {
        if (obj instanceof Paddle) {
            GameManager gm = GameManager.getInstance();
            Paddle paddle = gm.getPaddle();
            AnchorPane pane = (AnchorPane) paddle.getImageView().getParent();

            // 1️⃣ Xóa tất cả bóng hiện có KHÔNG trừ mạng
            for (Ball oldBall : new ArrayList<>(gm.getListBalls())) {
                pane.getChildren().remove(oldBall.getImageView());
            }
            gm.getListBalls().clear();

            // 2️⃣ Tạo 1 PierceBall mới ở giữa paddle, đứng yên
            PierceBall pierceBall = new PierceBall(
                    paddle.getX() + paddle.getWidth() / 2 - 10,
                    paddle.getY() - 20,
                    3,
                    0, -1
            );
            pierceBall.setStanding(true); // chờ bắn bằng chuột
            gm.getListBalls().add(pierceBall);

            // 3️⃣ Thêm vào scene graph
            pane.getChildren().add(pierceBall.getImageView());

            // 4️⃣ Đảm bảo mũi tên aiming vẫn ở trên cùng nếu đang hiển thị
            Node pauseMenu = pane.lookup("#pauseMenu");
            if (pauseMenu != null) pauseMenu.toFront();

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
