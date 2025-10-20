package Models;

import java.awt.*;

public class Ball extends MovableObject {
    private double speed, directionX, directionY;

    public Ball () {
        this.speed = 0;
        this.directionX = 0;
        this.directionY = 0;
    }

    public Ball(double speed, double directionX, double directionY) {
        this.speed = speed;
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public boolean checkCollision(GameObject other) {
        double leftA, leftB;
        double rightA, rightB;
        double topA, topB;
        double bottomA, bottomB;

        //Calculate the sides of rect A
        leftA = this.x;
        rightA = this.x + this.width;
        topA = this.y;
        bottomA = this.y + this.height;

        //Calculate the sides of rect B
        leftB = other.x;
        rightB = other.x + other.width;
        topB = other.y;
        bottomB = other.y + other.height;

        //If any of the sides from A are outside of B
        if( bottomA <= topB )
        {
            return false;
        }

        if( topA >= bottomB )
        {
            return false;
        }

        if( rightA <= leftB )
        {
            return false;
        }

        if( leftA >= rightB )
        {
            return false;
        }

        //If none of the sides from A are outside B
        return true;
    }

    public void bounceOff(GameObject other) {
        if (!checkCollision(other)) return;

        double ballCenterX = this.getX() + this.getWidth() / 2;
        double ballCenterY = this.getY() + this.getHeight() / 2;

        double otherCenterX = other.getX() + other.getWidth() / 2;
        double otherCenterY = other.getY() + other.getHeight() / 2;

        double dxDistance = ballCenterX - otherCenterX;
        double dyDistance = ballCenterY - otherCenterY;

        double overlapX = (this.getWidth() / 2 + other.getWidth() / 2) - Math.abs(dxDistance);
        double overlapY = (this.getHeight() / 2 + other.getHeight() / 2) - Math.abs(dyDistance);

        if (overlapX < overlapY) {
            // Va chạm theo trục X → đổi hướng X
            directionX *= -1;
            setDx(directionX * speed);

            // Đẩy ra khỏi vật để tránh dính
            if (dxDistance > 0) {
                setX(other.getX() + other.getWidth());
            } else {
                setX(other.getX() - getWidth());
            }
        } else {
            // Va chạm theo trục Y → đổi hướng Y
            directionY *= -1;
            setDy(directionY * speed);

            // Đẩy ra khỏi vật để tránh dính
            if (dyDistance > 0) {
                setY(other.getY() + other.getHeight());
            } else {
                setY(other.getY() - getHeight());
            }
        }
    }


    public void render(Graphics g) {

    }
}
