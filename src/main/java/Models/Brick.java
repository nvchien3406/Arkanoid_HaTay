package Models;

import javafx.scene.image.Image;

public class Brick extends GameObject {
    private final int maxHitPoints;
    private int hitPoints;
    private final String type;
    private final int frameCount;

    private final double frameWidth;
    private final double frameHeight;


    private final int breakStartFrame;
    private final int breakEndFrame;
    private boolean breaking = false;
    private boolean breakDone = false;

    private int currentFrameIndex = 0;

    // Tổng thời gian "tan biến" (ms) => chỉnh để nhanh/chậm
    private final double breakTotalMs = 500;   // ~0.2 giây
    private double breakElapsedMs = 0;
    private double breakOpacity = 1.0;         // 1→0 trong lúc tan

    public Brick(double x, double y, double w, double h,
                 int hitPoints, String type, String path, int frameCount) {
        super(x, y, w, h, path);
        this.hitPoints = hitPoints;
        this.maxHitPoints = hitPoints;
        this.type = type;

        this.frameCount = Math.max(1, frameCount);
        this.frameWidth  = image.getWidth() / this.frameCount;
        this.frameHeight = image.getHeight();

        this.breakStartFrame = Math.min(this.maxHitPoints, this.frameCount - 1); // 5
        this.breakEndFrame   = this.frameCount - 1;                              // 10
    }


    public boolean takeHit() {
        if (breaking || breakDone) return false;
        if (hitPoints > 0) hitPoints--;

        if (hitPoints <= 0) {
            // bắt đầu tan: nhảy ngay tới frame 6 (index 5)
            breaking = true;
            breakElapsedMs = 0;
            breakOpacity = 1.0;
            currentFrameIndex = breakStartFrame;
            return true;
        } else {
            // sứt mẻ dần: dùng frame 0..(start-1)
            int used = maxHitPoints - hitPoints; // 1..4
            currentFrameIndex = Math.min(used, breakStartFrame - 1);
            return false;
        }
    }

    /** Gọi mỗi tick với dt (ms) để nội suy frame + fade. */
    public void update(double dtMs) {
        if (!breaking || breakDone) return;

        breakElapsedMs += dtMs;
        double t = Math.min(1.0, breakElapsedMs / breakTotalMs); // 0→1
        breakOpacity = 1.0 - t;

        // Nội suy frame từ breakStartFrame → breakEndFrame theo t
        int span = breakEndFrame - breakStartFrame;              // vd: 5
        currentFrameIndex = breakStartFrame + (int)Math.floor(t * (span + 1));
        if (currentFrameIndex > breakEndFrame) currentFrameIndex = breakEndFrame;

        if (t >= 1.0) {
            breakDone = true;
            breaking = false;
        }
    }

    public boolean isDestroyed() { return hitPoints == 0; }
    public boolean isBreaking()  { return breaking; }
    public boolean isBreakDone() { return breakDone; }

    public int getFrameIndex()   { return currentFrameIndex; }
    public double getOpacity()   { return breakOpacity; } // dùng khi vẽ

    public Image getImage()      { return image; }
    public double getFrameWidth(){ return frameWidth; }
    public double getFrameHeight(){ return frameHeight; }
    public String getType()      { return type; }

    @Override public void update() { /* để trống; dùng update(dtMs) ở Controller */ }
}
