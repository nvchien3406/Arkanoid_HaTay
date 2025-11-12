package Models.PowerUp_Down;

import Models.Object.GameObject;
import Models.Object.MovableObject;
import Models.Paddle.Paddle;

public abstract class PowerUp extends MovableObject {
    protected String type;
    protected double duration;
    protected boolean active = false;
    protected boolean collected = false;
    protected double elapsedTime = 0;
    protected boolean expired = false;

    // Constructor mặc định
    public PowerUp() {
        super();
        this.type = "";
        this.duration = 0.0;
    }

    // Constructor đầy đủ
    public PowerUp(double x, double y, double width, double height, String path,
                   double dx, double dy, double speed, String type, double duration,
                   boolean active, boolean collected, double elapsedTime) {
        super(x, y, speed, width, height, path, dx, dy);
        this.type = type;
        this.duration = duration;
        this.active = active;
        this.collected = collected;
        this.elapsedTime = elapsedTime;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getDuration() { return duration; }
    public void setDuration(double duration) { this.duration = duration; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isCollected() { return collected; }
    public void setCollected(boolean collected) { this.collected = collected; }

    public boolean isExpired() { return expired; } // ⚡ cần cho GameManager lọc toRemove

    public void checkPaddleCollision(Paddle paddle) {
        if (!collected && checkCollision(paddle)) {
            collected = true;
            active = true;
            elapsedTime = 0;
            this.getImageView().setVisible(false); // ẩn item
            applyEffect(paddle);
        }
    }


    public void update(Paddle paddle) {
        if (!collected) {
            setY(getY() + getSpeed());
            getImageView().setLayoutY(getY());

            // Nếu rơi quá đáy màn hình → đánh dấu hết hạn
            if (getY() > 800) {
                expired = true;
                return;
            }
        }

        if (active) {
            elapsedTime += 1.0 / 60.0;

            // Hết hạn
            if (elapsedTime >= duration) {
                removeEffect(paddle);
                active = false;
                expired = true;
            }
        }
    }

    public abstract void applyEffect(GameObject gameObject);
    public abstract void removeEffect(GameObject gameObject);
}
