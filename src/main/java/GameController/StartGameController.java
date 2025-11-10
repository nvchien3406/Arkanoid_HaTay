package GameController;

import Models.*;
import Utils.SceneTransition;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StartGameController implements GameConstant{
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

    private Rectangle overlay;
    private boolean isPaused = false;

    public static final int ROWS = 14;
    public static final int COLS = 18;
    public static final int BRICK_WIDTH = 32;
    public static final int BRICK_HEIGHT = 16;
    public static final Pair<String, String>[] brickImages = new Pair[]{
            new Pair<>("/image/BlueBrick.png", "NormalBrick"),
            new Pair<>("/image/GreenBrick.png", "NormalBrick"),
            new Pair<>("/image/OrangeBrick.png", "NormalBrick"),
            new Pair<>("/image/PurpleBrick.png", "StrongBrick"),
            new Pair<>("/image/RedBrick.png", "StrongBrick"),
            new Pair<>("/image/YellowBrick.png", "StrongBrick"),
            new Pair<>("/image/SpecialBrick.png", "SpecialBrick")
    };
    public static final String[] paddleImages = {
            "/image/Paddle.png"
    };
    public static final String[] BallImages = {
            "/image/NormalBall.png",
            "/image/PierceBall.png"
    };
    public static final String[] powerUpImages = {
            "/image/ExpandPaddlePowerUp.png"
    };

    @FXML
    public List<Brick> LoadBrick() {
        List<Brick> bricks = new ArrayList();
        Random random = new Random();

        int[][] pattern = {
                {5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6, 5, 6},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 4, 4, 1, 1, 4, 4, 1, 1, 1, 1, 3, 3, 1, 1, 3, 3, 1},
                {1, 1, 1, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1},
                {1, 1, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 1, 1},
                {1, 4, 6, 4, 4, 6, 4, 1, 1, 1, 1, 3, 6, 3, 3, 6, 3, 1},
                {4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3},
                {4, 1, 4, 4, 4, 4, 1, 4, 1, 1, 3, 1, 3, 3, 3, 3, 1, 3},
                {1, 1, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 3, 3, 3, 3, 1, 1},
                {1, 1, 4, 1, 1, 4, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2, 0, 2}

        };

        for (int row = ROWS - 1; row >= 0; row--) {
            for (int col = 0; col < COLS; col++) {

                // Tạo ngẫu nhiên: 20% không có gạch
                //if (random.nextDouble() < 0.2) continue;

                double x = col * BRICK_WIDTH + 62;
                double y = row * BRICK_HEIGHT + 50;

                String imgPath = brickImages[pattern[row][col]].getKey();

                Brick brick;
                if (brickImages[pattern[row][col]].getValue().equals("NormalBrick")) {
                    brick = new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, imgPath);
                } else if (brickImages[pattern[row][col]].getValue().equals("StrongBrick")) {
                    brick = new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, imgPath);
                } else brick = new SpecialBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, imgPath);

                bricks.add(brick);
                startGamePane.getChildren().add(brick.getImageView());
            }
        }
        return bricks;
    }

    public void initialize() {
        GameManager gameManager = GameManager.getInstance();
        gameManager.startGame(this);

        createPauseMenu();

        Pause.setOnAction(e -> {
            showPauseMenu();
            gameManager.pauseGame();
        });

        resume.setOnAction(e -> resumeGame());
        restart.setOnAction(e -> restartGame());
        exit.setOnAction(e -> exitToMenu());
        setting.setOnAction(e -> settingGame());
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
        GameManager gameManager = GameManager.getInstance();
        gameManager.resumeGame(this); // Tiếp tục game loop
        hidePauseMenu();
    }

    private void restartGame() {
        hidePauseMenu();
        GameManager.getInstance().resetGameManager(this);
        Stage stage = getStage();
        SceneTransition.switchScene(stage, "startGame.fxml");
    }

    private void exitToMenu() {
        hidePauseMenu();
        SoundManager.StopSoundBackground();
        GameManager.getInstance().resetGameManager(this);
        Stage stage = getStage();
        SceneTransition.switchScene(stage, "menuGame.fxml");
    }

    private void settingGame() {
        SoundManager.StopSoundBackground();
        Stage stage = getStage();
        Scene currentScene = getScene();
        SettingsController.setBackScene(currentScene);
        SceneTransition.switchScene(stage, "settings.fxml");
    }

    @FXML
    public Paddle LoadPaddle() {
        double width = PADDLE_WIDTH;
        double height = PADDLE_HEIGHT;
        double startX = 550;
        double startY = 600;
        Paddle paddle = new Paddle(startX, startY, width, height, paddleImages[0], 0, 0,
                PADDLE_SPEED, false, false);

        startGamePane.getChildren().add(paddle.getImageView());
        return paddle;
    }

    @FXML
    public Ball LoadBall() {
        double size = BALL_SIZE;

        double startX = 550;
        double startY = 500;

        Ball mainBall = new Ball(startX , startY , 20 , 20 , BallImages[0] ,3 ,1 , 1 );
        GameManager gm = GameManager.getInstance();
        gm.getListBalls().add(mainBall);
        startGamePane.getChildren().add(mainBall.getImageView());

        return mainBall;
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
