package Models.Brick;

import GameController.GameConstant;
import GameController.GameManager;
import GameController.StartGameController;
import Models.Interface.CanTakeHit;
import Models.Player.Player;

public class NormalBrick extends BreakableBrick {
    public NormalBrick(double x, double y, double width, double height, String path) {
        super(x, y, width, height,1,"NormalBrick" , path);
    }
}
