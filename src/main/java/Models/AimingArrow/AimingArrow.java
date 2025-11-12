package Models.AimingArrow;
import Models.Ball.Ball;
import Models.Object.GameObject;
import javafx.scene.transform.Rotate;

/**
 * AimingArrow — đại diện cho mũi tên hướng bắn (aim indicator) của người chơi.
 *
 * - Chỉ hiển thị khi bóng đang đứng yên.
 * - Xoay theo hướng chuột.
 * - Di chuyển cùng bóng và paddle.
 * - Không có vận tốc (không tự di chuyển).
 */
public class AimingArrow extends GameObject {
    private final Rotate rotate;
    private final double anchorRatioY; // tỉ lệ neo đuôi mũi tên (0..1), ~0.9–0.96 là đẹp
    private boolean visible = false;

    public AimingArrow(double x, double y, double width, double height, String path) {
        this(x, y, width, height, path, 0.92); // mặc định neo ~ 92% chiều cao (gần đáy ảnh)
    }

    public AimingArrow(double x, double y, double width, double height, String path, double anchorRatioY) {
        super(x, y, width, height, path);
        getImageView().setPreserveRatio(true);
        getImageView().setOpacity(0.9);
        getImageView().setVisible(false);
        this.anchorRatioY = anchorRatioY;
        rotate = new Rotate(0, /*pivotX*/ 0, /*pivotY*/ 0);
        getImageView().getTransforms().add(rotate);
    }

    /** Xoay quanh pivot (pivot là tọa độ LOCAL, set trong followBall) */
    public void setAngle(double angle) {
        rotate.setAngle(angle);
    }

    public double getAngle() {
        return rotate.getAngle();
    }

    /** Bám đuôi tâm bóng và cập nhật pivot LOCAL cho quay quanh đúng tâm bóng */
    public void followBall(Ball ball) {
        double cx = ball.getX() + ball.getWidth() / 2.0;
        double cy = ball.getY() + ball.getHeight() / 2.0;

        double w = getImageView().getFitWidth();   // dùng kích thước hiển thị thực tế
        double h = getImageView().getFitHeight();

        // Đặt ảnh sao cho đuôi mũi tên (h*anchorRatioY) trùng tâm bóng
        getImageView().setLayoutX(cx - w / 2.0);
        getImageView().setLayoutY(cy - h * anchorRatioY);

        // Pivot của Rotate phải ở toạ độ LOCAL của ảnh:
        // LOCAL( w/2 , h*anchorRatioY ) <=> PARENT( cx , cy )
        rotate.setPivotX(w / 2.0);
        rotate.setPivotY(h * anchorRatioY);
    }

    public void show() {
        visible = true;
        getImageView().setVisible(true);
        getImageView().toFront();
    }

    public void hide() {
        visible = false;
        getImageView().setVisible(false);
    }

    public boolean isVisible() {
        return visible;
    }

    /** Ảnh gốc hướng lên; +90° để map từ trục X dương */
    public void updateDirection(double mouseX, double mouseY, Ball ball) {
        double cx = ball.getX() + ball.getWidth() / 2.0;
        double cy = ball.getY() + ball.getHeight() / 2.0;

        double dx = mouseX - cx;
        double dy = mouseY - cy;

        if (dy >= 0) dy = -1e-3;
        if (dx == 0) dx =  1e-3;

        double angleDeg = Math.toDegrees(Math.atan2(dy, dx)) + 90.0;
        setAngle(angleDeg);
    }

    @Override
    public void update() { /* no-op */ }
}


