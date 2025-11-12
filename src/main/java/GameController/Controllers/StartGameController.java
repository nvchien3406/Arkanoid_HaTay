package GameController.Controllers;

import GameController.GameConstants.GameConstant;
import GameController.Manager.GameManager;
import Models.Brick.*;
import Models.Level.LevelGame;
import Models.Paddle.Paddle;
import Models.Player.Player;
import Utils.SceneTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import Models.Ball.*;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class StartGameController{
    @FXML
    private AnchorPane startGamePane;
    @FXML
    private ListView<String> scoreBoard;
    @FXML
    private Label Score, TopScore, Level;
    @FXML
    private Button Pause;

    @FXML private VBox pauseMenu;
    @FXML private Button resume;
    @FXML private Button restart;
    @FXML private Button exit;
    @FXML private Button setting;
    @FXML private Line aimingArrow;
    @FXML private HBox livesHBox;

    private final List<ImageView> heartViews = new ArrayList<>();
    private Image heartImage;
    private Image heartEmptyImage; // tuỳ chọn: ảnh trái tim rỗng
    private int maxLives = 3;      // set tuỳ ý

    private Rectangle overlay;
    private boolean isPaused = false;

    @FXML
    public List<Brick> LoadBrick(LevelGame level) {
        List<Brick> bricks = new ArrayList();
        int[][] map = level.getCurrentLevel();
        int R = map.length;
        int C = map[0].length;

        double totalWidth = C * GameConstant.BRICK_WIDTH;
        double totalHeight = R * GameConstant.BRICK_HEIGHT;

        double startX = (GameConstant.PANE_WIDTH - totalWidth) / 2;
        double startY = 50;

        for (int row = R - 1; row >= 0; row--) {
            for (int col = 0; col < C; col++) {

                if (map[row][col] == 0) {continue;}

                double x = startX + col * GameConstant.BRICK_WIDTH;
                double y = startY + row * GameConstant.BRICK_HEIGHT;;

                String imgPath = GameConstant.brickImages[map[row][col] - 1].getKey();

                Brick brick;
                if (GameConstant.brickImages[map[row][col] - 1].getValue().equals("NormalBrick")) {
                    brick = new NormalBrick(x, y, GameConstant.BRICK_WIDTH, GameConstant.BRICK_HEIGHT, imgPath);
                } else if (GameConstant.brickImages[map[row][col] - 1].getValue().equals("StrongBrick")) {
                    brick = new StrongBrick(x, y, GameConstant.BRICK_WIDTH, GameConstant.BRICK_HEIGHT, imgPath);
                } else if (GameConstant.brickImages[map[row][col] - 1].getValue().equals("SpecialBrick")) {
                    brick = new SpecialBrick(x, y, GameConstant.BRICK_WIDTH, GameConstant.BRICK_HEIGHT, imgPath);
                } else {
                    int direction = (col > C/2 ? 1 : -1);
                    boolean isLeft = (col < C/2);
                    brick = new MovingBrick(x, y, GameConstant.BRICK_WIDTH, GameConstant.BRICK_HEIGHT, imgPath, direction, isLeft);
                }

                bricks.add(brick);
                startGamePane.getChildren().add(brick.getImageView());
            }
        }
        return bricks;
    }

    public void initialize() {
        GameManager gameManager = GameManager.getInstance();
        GameManager.getInstance().startGame(this);

        createPauseMenu();

        Pause.setOnAction(e -> {
            showPauseMenu();
            GameManager.getInstance().getGameFlowManager().pauseGame();
        });

        resume.setOnAction(e -> resumeGame());
        restart.setOnAction(e -> restartGame());
        exit.setOnAction(e -> exitToMenu());
        setting.setOnAction(e -> settingGame());

        try {
            heartImage = new Image(getClass().getResourceAsStream(GameConstant.heartImages));
            heartEmptyImage = new Image(getClass().getResourceAsStream(GameConstant.heartEmptyImages));
        } catch (Exception e) {
            e.printStackTrace();
            // fallback: không crash, tạo label text nếu thiếu ảnh
            heartImage = null;
        }

        initHearts(maxLives);

        Player player = GameManager.getInstance().getObjectManager().getPlayer();
        if (player != null) {
            updateLives(player.getLives());
        }
    }

    private void initHearts(int count) {
        livesHBox.getChildren().clear();
        heartViews.clear();

        for (int i = 0; i < count; i++) {
            ImageView iv = new ImageView();
            iv.setPreserveRatio(true);

            if (heartImage != null) iv.setImage(heartImage);

            heartViews.add(iv);
            livesHBox.getChildren().add(iv);
        }
    }

    public void updateLives(int playerLives) {
        // đảm bảo không âm
        if (playerLives < 0) playerLives = 0;

        // nếu không đủ ImageView (player có lives > max), ta có thể mở rộng
        if (playerLives > heartViews.size()) {
            int diff = playerLives - heartViews.size();
            for (int i = 0; i < diff; i++) {
                ImageView iv = new ImageView(heartImage);
                iv.setFitWidth(28);
                iv.setFitHeight(28);
                iv.setPreserveRatio(true);
                heartViews.add(iv);
                livesHBox.getChildren().add(iv);
            }
        }

        // set visible / đổi ảnh rỗng nếu muốn
        for (int i = 0; i < heartViews.size(); i++) {
            ImageView iv = heartViews.get(i);
            if (i < playerLives) {
                iv.setVisible(true);
                if (heartImage != null) iv.setImage(heartImage);
            } else {
                if (heartEmptyImage != null) {
                    iv.setImage(heartEmptyImage);
                    iv.setVisible(true);
                } else {
                    iv.setVisible(false);
                }
            }
        }
    }


    public void createPauseMenu() {
        // overlay mờ nền
        overlay = new Rectangle();
        overlay.widthProperty().bind(startGamePane.widthProperty());
        overlay.heightProperty().bind(startGamePane.heightProperty());
        overlay.setFill(Color.rgb(0, 0, 0, 0.5));
        overlay.setVisible(false);
        startGamePane.getChildren().add(overlay);
        startGamePane.getChildren().remove(pauseMenu); // đưa pauseMenu lên trên overlay
        startGamePane.getChildren().add(pauseMenu);
    }

    private void showPauseMenu() {
        isPaused = true;
        Pause.setText("RESUME");
        overlay.setVisible(true);
        pauseMenu.setVisible(true);

        // Ban đầu menu nhỏ và trong suốt
        //pauseMenu.setOpacity(0);
        pauseMenu.setScaleX(0.6);
        pauseMenu.setScaleY(0.6);

        // Hiệu ứng mờ dần
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), pauseMenu);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Hiệu ứng phóng to nhẹ
        ScaleTransition zoomIn = new ScaleTransition(Duration.millis(300), pauseMenu);
        zoomIn.setFromX(0.6);
        zoomIn.setFromY(0.6);
        zoomIn.setToX(1);
        zoomIn.setToY(1);

        fadeIn.play();
        zoomIn.play();
    }

    private void hidePauseMenu() {
        isPaused = false;
        Pause.setText("PAUSE");

        // Fade out menu
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), pauseMenu);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> {
            pauseMenu.setVisible(false);
            overlay.setVisible(false);
        });

        fadeOut.play();
    }

    private void resumeGame() {
        GameManager.getInstance().getSoundService().resumeSoundBackground();
        GameManager.getInstance().getGameFlowManager().resumeGame(this); // Tiếp tục game loop
        hidePauseMenu();
    }

    private void restartGame() {
        hidePauseMenu();
        GameManager.getInstance().resetGameManager(this, false);
        Stage stage = getStage();
        SceneTransition.switchScene(stage, "startGame/startGame.fxml");
    }

    private void exitToMenu() {
        hidePauseMenu();
        GameManager.getInstance().getSoundService().pauseSoundBackground();
        GameManager.getInstance().resetGameManager(this, false);
        Stage stage = getStage();
        SceneTransition.switchScene(stage, "menuGame/menuGame.fxml");
    }

    private void settingGame() {
        GameManager.getInstance().getSoundService().pauseSoundBackground();
        Stage stage = getStage();
        Scene currentScene = getScene();
        SettingsController.setBackScene(currentScene);
        SceneTransition.switchScene(stage, "settingGame/settings.fxml");
    }

    @FXML
    public Paddle LoadPaddle() {
        double width = GameConstant.PADDLE_WIDTH;
        double height = GameConstant.PADDLE_HEIGHT;
        double startX = GameConstant.PANE_WIDTH / 2 - GameConstant.PADDLE_WIDTH / 2;
        double startY = 600;
        Paddle paddle = new Paddle(startX, startY, width, height, GameConstant.paddleImages, 0, 0,
                GameConstant.PADDLE_SPEED, false, false);

        startGamePane.getChildren().add(paddle.getImageView());
        return paddle;
    }

    @FXML
    public Ball LoadBall() {
        double size = GameConstant.BALL_SIZE;

        double startX = 550;
        double startY = 500;

        Ball mainBall = new NormalBall(startX , startY , 20 , 20 , GameConstant.BallImages[0] ,3 ,1 , 1 );
        GameManager gm = GameManager.getInstance();
        GameManager.getInstance().getObjectManager().getListBalls().add(mainBall);
        startGamePane.getChildren().add(mainBall.getImageView());

        return mainBall;
    }

    public void animateLevelUp(int newLevel) {
        if (Level == null) return;

        Level.setText(String.valueOf(newLevel));
        Level.setTextFill(Color.WHITE); // trắng sáng rõ

        // Hiệu ứng nảy nhỏ
        ScaleTransition scale = new ScaleTransition(Duration.millis(400), Level);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.3);
        scale.setToY(1.3);

        scale.setAutoReverse(true);
        scale.setCycleCount(2);

        scale.play();
    }

    public Line LoadAimingRow() {
        aimingArrow = new Line();
        aimingArrow.setStrokeWidth(3);
        aimingArrow.setStroke(Color.CYAN);
        aimingArrow.setVisible(false);
        startGamePane.getChildren().add(aimingArrow);
        return aimingArrow;
    }


    public AnchorPane getStartGamePane() {
        return startGamePane;
    }

    // Cập nhật điểm hiện tại
    public void updateCurrentScore(int score) {
        Score.setText(String.valueOf(score));
    }

    public void updateCurrentTopScore(int topScore) {
        TopScore.setText(String.valueOf(topScore));
    }

    // Hiển thị danh sách top 10
    public void updateHighScores(List<String> topScores) {
        scoreBoard.getItems().setAll(topScores);
    }

    public AnchorPane getStartGame() {
        return startGamePane;
    }

    public Stage getStage() {
        return (Stage) startGamePane.getScene().getWindow();
    }

    public Scene getScene() {
        return (Scene) startGamePane.getScene();
    }
}
