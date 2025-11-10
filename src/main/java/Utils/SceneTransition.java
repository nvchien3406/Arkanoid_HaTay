package Utils;

import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class SceneTransition {
    public static void switchScene(Stage stage, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneTransition.class.getResource("/GameController/" + fxmlFile));
            Parent root = loader.load();

            // 🔹 Lấy tên CSS tương ứng với file FXML (nếu có)
            String cssName = fxmlFile.replace(".fxml", ".css");
            var cssUrl = SceneTransition.class.getResource("/GameController/" + cssName);
            if (cssUrl != null) {
                root.getStylesheets().add(cssUrl.toExternalForm());
            }

            SceneTransition.fadeSwitch(stage, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T switchSceneWithController(Stage stage, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneTransition.class.getResource("/GameController/" + fxmlFile));
            Parent root = loader.load();

            // 🔹 Lấy tên CSS tương ứng với file FXML (nếu có)
            String cssName = fxmlFile.replace(".fxml", ".css");
            var cssUrl = SceneTransition.class.getResource("/GameController/" + cssName);
            if (cssUrl != null) {
                root.getStylesheets().add(cssUrl.toExternalForm());
            }

            SceneTransition.fadeSwitch(stage, root);
            return loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void fadeSwitch(Stage stage, Parent nextRoot) {
        try {
            Scene oldScene = stage.getScene();

            if (oldScene == null) {
                // Chạy lần đầu, chưa có scene
                Scene scene = new Scene(nextRoot);
                stage.setScene(scene);
            } else {
                // Tạo Scene mới cho root tiếp theo
                Scene nextScene = new Scene(nextRoot);

                // Fade out Scene cũ
                Parent oldRoot = oldScene.getRoot();
                FadeTransition fadeOut = new FadeTransition(Duration.millis(200), oldRoot);
                fadeOut.setFromValue(1);
                fadeOut.setToValue(0);

                fadeOut.setOnFinished(e -> {
                    // Khi fade out xong, set Scene mới với root mới
                    stage.setScene(nextScene);

                    // Fade in root mới
                    nextRoot.setOpacity(0);
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(300), nextRoot);
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.play();
                });

                fadeOut.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
