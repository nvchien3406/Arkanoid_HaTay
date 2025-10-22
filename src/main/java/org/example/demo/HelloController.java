package org.example.demo;

import Models.Ball;
import Models.Brick;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HelloController {

    @FXML
    private AnchorPane gamePane;

    // --- Model ---
    private Ball ball;
    private final List<Brick> bricks = new ArrayList<>();

    // --- View ---
    @FXML
    private Circle ballView; // vẫn dùng Circle như cũ cho bóng

    // Canvas để vẽ BRICK (model-only)
    private Canvas canvas;
    private GraphicsContext gc;

    // --- Game Loop ---
    private Timeline timeline;

    private static final double DT_MS = 10.0;

    public void initialize() {
        // 0) Tạo Canvas (vẽ bricks) và add vào gamePane
        canvas = new Canvas(800, 600); // kích thước tạm; sẽ bind ngay bên dưới
        gc = canvas.getGraphicsContext2D();
        gamePane.getChildren().add(0, canvas); // thêm dưới cùng để bóng (Circle) nổi phía trên

        // bind canvas theo kích thước pane để không bị co kéo
        canvas.widthProperty().bind(gamePane.widthProperty());
        canvas.heightProperty().bind(gamePane.heightProperty());

        // 1) Khởi tạo gạch (model-only)
        createBricks();

        // 2) Khởi tạo bóng (Model)
        ball = new Ball(4, 1, -1); // speed=4, hướng (1, -1)

        // 3) Đồng bộ Model từ View (View đang lưu TÂM; Model lưu góc trái-trên + kích thước)
        double r = ballView.getRadius();
        ball.setWidth(r * 2);
        ball.setHeight(r * 2);
        ball.setX(ballView.getCenterX() - r);
        ball.setY(ballView.getCenterY() - r);

        // 4) Vận tốc ban đầu (dx, dy) khớp speed*direction
//        ball.setDx(4);
//        ball.setDy(-4);

        // 5) Game loop
        setupGameLoop();
        timeline.play();
    }

    private void createBricks() {
        int rows = 5;
        int cols = 10;
        double brickWidth = 32;
        double brickHeight = 16;

        int frameCount = 11;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = 50 + col * (brickWidth);
                double y = 50 + row * (brickHeight);

                Brick brick = new Brick(
                        x, y,
                        brickWidth, brickHeight,
                        6, "strong",
                        "/images/BlueBrick.png",
                        frameCount
                );
                bricks.add(brick);
            }
        }
    }

    private void setupGameLoop() {
        // ~100 FPS (10ms). Có thể đổi 16ms ~60FPS
        timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> gameTick()));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    private void gameTick() {
        ball.update();

        double r = ballView.getRadius();
        ballView.setCenterX(ball.getX() + r);
        ballView.setCenterY(ball.getY() + r);

        checkWallCollisions();
        checkBrickCollisions();

        // ✅ Tiến animation tan vỡ
        for (Brick b : bricks) {
            if (b.isBreaking()) {
                b.update(DT_MS);
            }
        }

        render();

        // ✅ Xóa những viên đã tan xong
        bricks.removeIf(Brick::isBreakDone);

        if (bricks.isEmpty()) {
            timeline.stop();
            System.out.println("You Win!");
        }
    }

    private void render() {
        // clear toàn bộ canvas mỗi frame
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Brick b : bricks) {
            int fi = b.getFrameIndex();
            double sx = fi * b.getFrameWidth();

            // nếu đang tan → giảm alpha (opacity)
            if (b.isBreaking()) {
                gc.setGlobalAlpha(b.getOpacity());   // 1 → 0
            } else {
                gc.setGlobalAlpha(1.0);
            }

            gc.drawImage(
                    b.getImage(),
                    sx, 0, b.getFrameWidth(), b.getFrameHeight(),    // source rect
                    b.getX(), b.getY(), b.getWidth(), b.getHeight()  // dest rect
            );
        }

        // khôi phục alpha cho các vẽ khác
        gc.setGlobalAlpha(1.0);
    }

    /**
     * Va chạm tường bằng AABB: Model dùng (x,y,width,height)
     */
    private void checkWallCollisions() {
        Bounds bounds = gamePane.getBoundsInLocal();
        double x = ball.getX();
        double y = ball.getY();
        double w = ball.getWidth();
        double h = ball.getHeight();
        final double EPS = 0.5;


        boolean hitLeft   = x <= bounds.getMinX();
        boolean hitRight  = (x + w) >= bounds.getMaxX();
        boolean hitTop    = y <= bounds.getMinY();
        boolean hitBottom = (y + h) >= bounds.getMaxY();

        if (hitLeft) {
            ball.setX(bounds.getMinX());
            ball.invertDirX();   // hướng sang phải
        } else if (hitRight) {
            ball.setX(bounds.getMaxX() - w);
            ball.invertDirX();  // hướng sang trái
        }

        if (hitTop) {
            ball.setY(bounds.getMinY());
            ball.invertDirY();   // hướng đi xuống
        } else if (hitBottom) {
            ball.setY(bounds.getMaxY() - h);
            ball.invertDirY(); // hướng đi lên
            // Nếu muốn Game Over khi chạm đáy, thay vì nảy lại:
            // timeline.stop(); System.out.println("Game Over");
        }
    }

    /**
     * Va chạm gạch với model-only bricks
     */
    private void checkBrickCollisions() {
        Iterator<Brick> iterator = bricks.iterator();
        while (iterator.hasNext()) {
            Brick brick = iterator.next();

            if (ball.checkCollision(brick)) {
                // 1) Nảy bóng (logic trong Ball)
                ball.bounceOff(brick);

                // 2) Trừ máu gạch (đồng thời đổi frameIndex)
                boolean destroyed = brick.takeHit();

                // 4) Chỉ xử lý 1 viên/ tick để tránh “kẹt”
                break;
            }
        }
    }
}
