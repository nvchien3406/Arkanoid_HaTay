package GameController;
import Models.*;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager instance;
    private Paddle paddle;
    private Ball ball;
    private List<Brick> listBricks;
    private List<PowerUp> listPowerUps;
    private AnimationTimer gameTimer;
    private Player player ;
    private ScoreDAO scoreDAO;
    private boolean gameState;


    // 🔒 Constructor private: chỉ cho phép tạo nội bộ
    private GameManager() {
        listPowerUps = new ArrayList<>();
    }

    // 🔹 Singleton getter
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }




    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }

    public List<Brick> getListBricks() {
        return listBricks;
    }

    public void setListBricks(List<Brick> listBricks) {
        this.listBricks = listBricks;
    }

    public List<PowerUp> getListPowerUps() {
        return listPowerUps;
    }

    public void setListPowerUps(List<PowerUp> listPowerUps) {
        this.listPowerUps = listPowerUps;
    }

    public boolean isGameState() {
        return gameState;
    }

    public void setGameState(boolean gameState) {
        this.gameState = gameState;
    }

    public void removePowerUp(PowerUp powerUp) {
        if (listPowerUps != null && listPowerUps.contains(powerUp)) {
            // 1. Xóa khỏi danh sách quản lý
            listPowerUps.remove(powerUp);

            // 2. Ẩn hoặc xóa hình ảnh khỏi màn hình (nếu còn hiển thị)
            if (powerUp.getImageView() != null) {
                powerUp.getImageView().setVisible(false);
            }
        }
    }


    public void startGame(StartGameController controller) {
        player = new Player("Bao" ,0 , 3) ;
        scoreDAO = new ScoreDAO();
        gameState = true;

        // Load đối tượng lên màn
        this.listBricks = controller.LoadBrick();
        this.paddle = controller.LoadPaddle();
        this.ball = controller.LoadBall();

        // 🔹 Load ảnh surround brick
        Image surroundImage = new Image(getClass().getResourceAsStream("/image/SurroundBrick.png"));
        ImageView surroundView = new ImageView(surroundImage);
        // 🔹 Đặt kích thước & vị trí
        surroundView.setFitWidth(603);
        surroundView.setFitHeight(800);
        surroundView.setLayoutX(287);  // ví dụ: tọa độ X giữa màn hình
        surroundView.setLayoutY(32);  // ví dụ: tọa độ Y giữa màn hình

        // 🔹 Thêm lên AnchorPane
        controller.getStartGame().getChildren().add(surroundView);

        // 🔹 Lấy Scene để bắt phím
        Scene scene = controller.getStartGamePane().getScene();
        if (scene != null) {
            setupKeyControls(scene);
        } else {
            // Nếu Scene chưa sẵn sàng (gặp khi load FXML), gắn listener
            controller.getStartGamePane().sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) setupKeyControls(newScene);
            });
        }

        // 🔹 Bắt đầu vòng lặp game
        startGameLoop(controller);
    }

    public void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) paddle.moveL = true;
            if (event.getCode() == KeyCode.RIGHT) paddle.moveR = true;
            if (event.getCode() == KeyCode.SPACE) {
                if (ball.isStanding()) {
                    ball.setStanding(false);
                    ball.setDirectionX((Math.random() < 0.5 ? -(0.4 + Math.random() * 0.6) : 0.4 + Math.random() * 0.));
                    ball.setDirectionY(-1);
                }

            };
        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) paddle.moveL = false;
            if (event.getCode() == KeyCode.RIGHT) paddle.moveR = false;
        });

        // Bảo đảm focus để nhận phím
        scene.getRoot().requestFocus();
    }

    public void disableKeyControls(Scene scene) {
        scene.setOnKeyPressed(null);
        scene.setOnKeyReleased(null);
    }


    public void updateGame(StartGameController controller){
        checkCollisions();
        ball.moveBallWithPaddle(paddle);
        paddle.movePaddle(controller);
        controller.updateCurrentScore(player.getScore());
        List<String> topscores = scoreDAO.getHighScores();
        controller.updateHighScores(topscores);
        paddle.movePaddle(controller);

        // update powerups và check collision
        if (listPowerUps != null && !listPowerUps.isEmpty()) {
            // update tất cả trước
            for (PowerUp p : new ArrayList<>(listPowerUps)) {
                p.update();                // rơi xuống
                p.checkPaddleCollision(paddle); // ăn vật phẩm
//                if (p.getY() > 800) {
//                    // ẩn/đánh dấu để dọn
//                    p.getImageView().setVisible(false);
//                    p.setCollected(true);  // hoặc set some flag
//                }
            }

            // sau khi update xong, dọn powerup đã expired (đã removeEffect xong)
            List<PowerUp> toRemove = new ArrayList<>();
            for (PowerUp p : listPowerUps) {
                if (p.isExpired()) {
                    toRemove.add(p);
                }
            }
            listPowerUps.removeAll(toRemove);
        }
    }


    public void handelInput(){

    }

    public void checkCollisions(){
        ball.checkPaddleCollision(paddle);
        ball.checkBrickCollision(listBricks , player);
        ball.checkWallCollision(paddle , player);
    }

    public void gameOver(StartGameController controller){
        paddle = null;
        ball = null;
        gameState = false;
        gameTimer.stop();
        disableKeyControls(controller.getStartGamePane().getScene());
        scoreDAO.insertScore(player.getPlayerName(),  player.getScore());
        List<String> topscores = scoreDAO.getHighScores();
        controller.updateHighScores(topscores);
        player = null;
    }

    private void startGameLoop(StartGameController controller) {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(player.playerIsAlive()){
                    updateGame(controller);
                }
                else{
                    gameOver(controller);
                }
            }
        };
        gameTimer.start();
    }
}
