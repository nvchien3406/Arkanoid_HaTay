package org.example.demo;

import Models.Ball;
import Models.Brick;
import Models.GameObject; // Giả định lớp này tồn tại
import Models.MovableObject; // Giả định lớp này tồn tại
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HelloController {

    @FXML
    private AnchorPane gamePane;

    // --- Model ---
    private Ball ball; // Model logic từ package Models
    private List<Brick> bricks = new ArrayList<>(); // Danh sách model gạch

    // --- View ---
    @FXML
    private Circle ballView; // View (hình ảnh) của quả bóng

    // --- Game Loop ---
    private Timeline timeline;

    public void initialize() {
        // 1. Khởi tạo gạch (giữ nguyên code của bạn)
        createBricks();

        // 2. Khởi tạo Ball (Model)
        // Dùng constructor Ball(speed, directionX, directionY)
        ball = new Ball(4, 1, -1); // Tốc độ 4, hướng (1, -1) (lên trên, bên phải)

//        // 3. Khởi tạo Ball (View)
//        double startX = 300;
//        double startY = 300;
//        double radius = 8;
//        ballView = new Circle(startX, startY, radius, Color.WHITE);

        // 4. Đồng bộ Model với View
        // Giả định Ball (MovableObject) có các setter này
        ball.setX(ballView.getCenterX() - ballView.getRadius());
        ball.setY(ballView.getCenterY() - ballView.getRadius());
        ball.setWidth(ballView.getRadius() * 2);
        ball.setHeight(ballView.getRadius() * 2);

        // Thiết lập vận tốc ban đầu cho model
        // Chúng ta phải giả định Ball có các phương thức get/set cho speed/direction
        // HOẶC MovableObject có setDx/setDy
        // Dựa trên hàm bounceOff, có vẻ nó dùng setDx/setDy.

        // Vì Ball.java không có getters, chúng ta sẽ hardcode vận tốc ban đầu
        // khớp với speed và direction ở trên
        ball.setDx(4); // speed * directionX
        ball.setDy(-4); // speed * directionY

        // 6. Khởi tạo và chạy Game Loop
        setupGameLoop();
        timeline.play();
    }

    /**
     * Tạo các viên gạch
     */
    private void createBricks() {
        int rows = 5;
        int cols = 10;
        double brickWidth = 32;
        double brickHeight = 16;
        double spacing = 5; // Thêm khoảng cách cho đẹp

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = 50 + col * (brickWidth + spacing);
                double y = 50 + row * (brickHeight + spacing);

                Brick brick = new Brick(x, y, brickWidth, brickHeight, 6, "strong", "/images/BlueBrick.png");
                gamePane.getChildren().add(brick.getImageView());
                bricks.add(brick);
            }
        }
    }

    /**
     * Thiết lập Timeline (game loop)
     */
    private void setupGameLoop() {
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> gameTick()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Hàm này được gọi mỗi 10ms
     */
    private void gameTick() {
        // 1. Cập nhật vị trí logic của Ball (Model)
        // Giả định MovableObject có hàm update() hoặc chúng ta tự cập nhật
        // ball.update(); // Nếu có
        // Hoặc tự cập nhật:
        ball.update();


        // 2. Cập nhật vị trí của Ball (View)
        // Vị trí của Circle được tính bằng tâm (CenterX, CenterY)
        ballView.setCenterX(ball.getX());
        ballView.setCenterY(ball.getY());

        // 3. Kiểm tra va chạm
        checkWallCollisions();
        checkBrickCollisions();

        // 4. Kiểm tra điều kiện thắng
        if (bricks.isEmpty()) {
            timeline.stop();
            System.out.println("You Win!");
        }
    }

    /**
     * Kiểm tra va chạm với các bức tường
     */
    private void checkWallCollisions() {
        Bounds bounds = gamePane.getBoundsInLocal();
        double radius = ballView.getRadius();

        // Giả định getX() và getY() của Ball là tâm
        boolean rightBorder = ball.getX() >= (bounds.getMaxX() - radius);
        boolean leftBorder = ball.getX() <= (bounds.getMinX() + radius);
        boolean bottomBorder = ball.getY() >= (bounds.getMaxY() - radius);
        boolean topBorder = ball.getY() <= (bounds.getMinY() + radius);

        if (rightBorder || leftBorder) {
            ball.setDx(ball.getDx() * -1); // Đảo hướng X
        }
        if (bottomBorder || topBorder) {
            ball.setDy(ball.getDy() * -1); // Đảo hướng Y
        }

        // (Tùy chọn) Xử lý Game Over nếu chạm đáy
        // if (bottomBorder) {
        //    timeline.stop();
        //    System.out.println("Game Over");
        // }
    }

    /**
     * Kiểm tra va chạm với gạch
     */
    private void checkBrickCollisions() {
        // Dùng Iterator để có thể xóa phần tử trong lúc lặp
        Iterator<Brick> iterator = bricks.iterator();
        while (iterator.hasNext()) {
            Brick brick = iterator.next();

            // Dùng hàm checkCollision của Ball
            if (ball.checkCollision(brick)) {

                // Dùng logic nảy bóng của Ball
                ball.bounceOff(brick);

                // Xóa gạch (cả view và model)
                gamePane.getChildren().remove(brick.getImageView());
                iterator.remove();

                // Chỉ xử lý 1 va chạm gạch mỗi frame để tránh lỗi vật lý
                break;
            }
        }
    }
}