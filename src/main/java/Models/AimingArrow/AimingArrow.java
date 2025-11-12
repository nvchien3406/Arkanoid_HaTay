package Models.AimingArrow;
import Models.Ball.Ball;
import Models.Object.GameObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * AimingArrow — đại diện cho mũi tên hướng bắn (aim indicator) của người chơi.
 *
 * - Chỉ hiển thị khi bóng đang đứng yên.
 * - Xoay theo hướng chuột.
 * - Di chuyển cùng bóng và paddle.
 * - Không có vận tốc (không tự di chuyển).
 */
public class AimingArrow extends GameObject {
    private double angle = -90; // Mặc định hướng lên
    private boolean visible = false;

    public AimingArrow(double x, double y, double width, double height, String path) {
        super(x, y, width, height, path);
        getImageView().setPreserveRatio(true);
        getImageView().setOpacity(0.9);
        getImageView().setVisible(false);
    }

    /** Cập nhật góc xoay của mũi tên (theo độ) */
    public void setAngle(double angle) {
        this.angle = angle;
        getImageView().setRotate(angle);
    }

    public double getAngle() {
        return angle;
    }

    /** Cho phép mũi tên bám theo vị trí của bóng */
    public void followBall(Ball ball) {
        double newX = ball.getX() + ball.getWidth() / 2 - getWidth() / 2;
        double newY = ball.getY() - getHeight() + 5; // Dịch nhẹ cho cân đối
        setX(newX);
        setY(newY);
        getImageView().setLayoutX(newX);
        getImageView().setLayoutY(newY);
    }

    /** Hiển thị mũi tên */
    public void show() {
        visible = true;
        getImageView().setVisible(true);
    }

    /** Ẩn mũi tên */
    public void hide() {
        visible = false;
        getImageView().setVisible(false);
    }

    public boolean isVisible() {
        return visible;
    }

    public void updateDirection(double mouseX, double mouseY, Ball ball) {
        // Tâm quả bóng làm gốc hướng
        final double cx = ball.getX() + ball.getWidth()  / 2.0;
        final double cy = ball.getY() + ball.getHeight() / 2.0;

        double dx = mouseX - cx;
        double dy = mouseY - cy;

        // Ép chỉ cho phép hướng lên (dy âm). Tránh đúng 0 gây lỗi chia/atan2 không ổn định
        if (dy >= 0)  dy = -1e-3;
        if (dx == 0)  dx =  1e-3;

        // Nếu quá sát tâm thì giữ nguyên góc cũ (tránh giật)
        double len2 = dx*dx + dy*dy;
        if (len2 < 1e-6) return;

        // atan2 trả về góc với 0° là hướng sang phải.
        // Ảnh mũi tên của bạn 0° là hướng lên, nên +90° để map đúng trục.
        double angleDeg = Math.toDegrees(Math.atan2(dy, dx)) + 90.0;

        setAngle(angleDeg);
    }

    @Override
    public void update() {
        // Không có logic frame-by-frame
    }
}


