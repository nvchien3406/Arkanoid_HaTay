package GameController;

import GameController.Manager.GameManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.prefs.Preferences;

public class SettingsController {

    @FXML private Slider volumeSlider , audioClipSlider;
    @FXML private CheckBox muteCheckBox;
    @FXML private Label volumeLabel, audioClipLabel;

    private static final Preferences prefs = Preferences.userNodeForPackage(SettingsController.class);
    private static final String PREF_VOLUME = "volume";
    private static final String PREF_AUDIOCLIP = "audioClip";
    private static final String PREF_MUTED = "muted";

    private static String fxmlBack;
    private static Scene backScene;// FXML muốn trở về

    @FXML
    public void initialize() {
        // Load saved settings
        double volume = prefs.getDouble(PREF_VOLUME, 0);
        double audioClip = prefs.getDouble(PREF_AUDIOCLIP, 1);
        boolean muted = prefs.getBoolean(PREF_MUTED, false);

        volumeSlider.setValue(volume);
        audioClipSlider.setValue(audioClip);
        muteCheckBox.setSelected(muted);
        updateSettings(volume ,audioClip);

        // Khi kéo thanh volumeSlider
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSettings(newVal.doubleValue(), audioClipSlider.getValue());
        });

        // Khi kéo thanh audioClipSlider
        audioClipSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateSettings(volumeSlider.getValue(), newVal.doubleValue());
        });

        // Khi tick / bỏ tick mute
        muteCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            updateSettings(volumeSlider.getValue(), audioClipSlider.getValue());
        });
    }

    private void updateSettings(double volume , double audioClip) {
        volumeLabel.setText((int) (volume * 100) + "%");
        audioClipLabel.setText((int) (audioClip * 100) + "%");
        // Nếu bạn có SoundManager, gọi cập nhật âm lượng ở đây:
        if(muteCheckBox.isSelected()) {
            GameManager.getInstance().getSoundService().setMasterVolume(0 , 0);
        }
        else{
            GameManager.getInstance().getSoundService().setMasterVolume(volume , audioClip);
        }
    }

    @FXML
    private void onSave() {
        double volume = volumeSlider.getValue();
        double audioClip = audioClipSlider.getValue();
        boolean muted = muteCheckBox.isSelected();

        prefs.putDouble(PREF_VOLUME, volume);
        prefs.putDouble(PREF_AUDIOCLIP, audioClip);
        prefs.putBoolean(PREF_MUTED, muted);

        // Áp dụng vào SoundManager (nếu có)
         //SoundManager.setMasterVolume(muted ? 0 : volume);
        System.out.println("Settings saved: volume=" + volume + "audioClip=" + audioClip + " muted=" + muted);

//        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Đã lưu cài đặt!", ButtonType.OK);
//        alert.showAndWait();
    }

    @FXML
    private void onBack() {
        if (backScene != null) {
            Stage stage = getStage();
            stage.setScene(backScene); // Quay về Scene gốc, không reset

            backScene.getRoot().setOpacity(1);
        }
        else {
            System.out.println("Back Scene is null");
        }
    }

    public Stage getStage() {
        Node n = volumeSlider;
        if (n == null || n.getScene() == null) return null;
        return (Stage) n.getScene().getWindow();
    }

//    public static void setBackScene(String fxml) {
//        fxmlBack = fxml;
//    }

    public static void setBackScene(Scene scene) {
        backScene = scene;
    }

    public static Preferences getPrefs() {
        return prefs;
    }

    public static void LoadSettings() {
        //Preferences pref = SettingsController.getPrefs();

        boolean muted = prefs.getBoolean("muted", false);
        double volume = prefs.getDouble("volume", 0.3);
        double audioClip = prefs.getDouble("audioClip", 1);

        System.out.println("muted is " + muted + " Volume is " + volume + " audioClip is " + audioClip);

        if(muted){
            GameManager.getInstance().getSoundService().setMasterVolume(0 , 0);
        }
        else{
            GameManager.getInstance().getSoundService().setMasterVolume(volume , audioClip);
        }
    }
}
