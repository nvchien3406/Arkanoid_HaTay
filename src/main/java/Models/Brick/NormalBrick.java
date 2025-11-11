package Models.Brick;

import GameController.GameConstant;
import GameController.GameManager;
import GameController.StartGameController;
import Models.Interface.CanTakeHit;
import Models.Player.Player;

public class NormalBrick extends Brick implements CanTakeHit {

    public NormalBrick() {
        super();
    }

    public NormalBrick(double x, double y, double width, double height, String path) {
        super(x, y, width, height,1,"NormalBrick" , path);
    }

    public void takeHit() {
        if (breaking || isDestroyed() ) return;

        hitPoints--;
        if (hitPoints > 0) {
            update();
        } else {
            startBreakAnimation();

        }
    }

    public void addScore(Brick brick, Player player, StartGameController controller) {
        player.setScore(player.getScore() + GameConstant.addScore);
        double popupX = brick.getX() + brick.getWidth() / 2;
        double popupY = brick.getY() + brick.getHeight() / 2;
        GameManager.getInstance().showScorePopup(controller, popupX, popupY, GameConstant.addScore);
    }
}
