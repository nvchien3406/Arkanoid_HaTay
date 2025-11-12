package Models.AimingArrow;

import Models.Ball.Ball;
import Models.Object.GameObject;
import javafx.scene.transform.Rotate;

public class AimingArrow extends GameObject {
    private final Rotate rotate;
    private final double anchorRatioY; // tỉ lệ neo đuôi mũi tên (0..1), ~0.9–0.96 là đẹp
    private boolean visible = false;

    public AimingArrow(double x, double y, double width, double height, String path) {
        this(x, y, width, height, path, 0.92); // mặc định neo ~ 92% chiều cao (gần đáy ảnh)
    }

    public AimingArrow(double x, double y, double width, double height, String path, double anchorRatioY) {
        super(x, y, width, height, path);
        this.anchorRatioY = anchorRatioY;

        imageView.setPreserveRatio(true);
        imageView.setOpacity(0.9);
        imageView.setVisible(false);

        rotate = new Rotate(0, /*pivotX*/ 0, /*pivotY*/ 0);
        imageView.getTransforms().add(rotate);
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

        double w = imageView.getFitWidth();   // dùng kích thước hiển thị thực tế
        double h = imageView.getFitHeight();

        // Đặt ảnh sao cho đuôi mũi tên (h*anchorRatioY) trùng tâm bóng
        imageView.setLayoutX(cx - w / 2.0);
        imageView.setLayoutY(cy - h * anchorRatioY);

        // Pivot của Rotate phải ở toạ độ LOCAL của ảnh:
        // LOCAL( w/2 , h*anchorRatioY ) <=> PARENT( cx , cy )
        rotate.setPivotX(w / 2.0);
        rotate.setPivotY(h * anchorRatioY);
    }

    public void show() {
        visible = true;
        imageView.setVisible(true);
        imageView.toFront();
    }

    public void hide() {
        visible = false;
        imageView.setVisible(false);
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
