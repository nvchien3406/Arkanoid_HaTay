package GameController;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.prefs.Preferences;

public class SoundManager {
    private static MediaPlayer soundBackground ;
    private static MediaPlayer soundMenuBackground ;

    private static AudioClip breakClip;
    private static AudioClip hitClip;
    private static AudioClip explosionClip;
    private  static AudioClip soundLosing;
    //private  static AudioClip mouseClick;

    public static void LoadSound() {
        try {
            //Sound BackGround
            String path = SoundManager.class.getResource("/sounds/S31-Night-Prowler.mp3").toExternalForm();
            Media media = new Media(path);
            soundBackground = new MediaPlayer(media);

            String path2 = SoundManager.class.getResource("/sounds/menuMusic.mp3").toExternalForm();
            Media media2 = new Media(path2);
            soundMenuBackground = new MediaPlayer(media2);

            //Sound audioclip
            breakClip = new AudioClip(SoundManager.class.getResource("/sounds/break.mp3").toExternalForm());
            hitClip = new AudioClip(SoundManager.class.getResource("/sounds/hit.wav").toExternalForm());
            explosionClip = new AudioClip(SoundManager.class.getResource("/sounds/explosion.mp3").toExternalForm());
            soundLosing = new AudioClip(SoundManager.class.getResource("/sounds/losing.mp3").toExternalForm());
            //mouseClick = new AudioClip(SoundManager.class.getResource("/sounds/mouseClick2.wav").toExternalForm());
        } catch (Exception e) {
            throw new RuntimeException("lỗi load âm thanh" , e);
        }
    }

    public static void PlaySoundBackground() {
        try {
            soundBackground.setCycleCount(MediaPlayer.INDEFINITE);
            soundBackground.play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void StopSoundBackground() {
        if(soundBackground != null) {
            soundBackground.stop();
            System.out.println("đã dừng nhạc nền");
        }
    }

    public static void PauseSoundBackground() {
        if(soundBackground != null) {
            soundBackground.pause();
        }
    }

    public static void ResumeSoundBackground() {
        if(soundBackground != null) {
            soundBackground.play();
        }
    }

    public static void PlaySoundMenuBackground() {
        try {
            soundMenuBackground.setCycleCount(MediaPlayer.INDEFINITE);
            soundMenuBackground.play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void StopSoundMenuBackground() {
        if(soundMenuBackground != null) {
            soundMenuBackground.stop();
            System.out.println("đã dừng nhạc nền menu");
        }
    }

    public static void PlayHit() {
        if(hitClip != null) {
            hitClip.play();
        }
    }

    public static void PlayBreak() {
        if(breakClip != null) {
            breakClip.play();
        }
    }

    public static void PlayExplosion(){
        if(explosionClip  != null) {
            explosionClip.play();
        }
    }

    public static void PlayLosing(){
        if(soundLosing != null) {
            soundLosing.play();
        }
    }

//    public static void PlayMouseClick(){
//        if(mouseClick != null) {
//            mouseClick.play();
//        }
//    }

    public static void setMasterVolume(double volume , double audioClip) {
        soundBackground.setVolume(volume);
        soundMenuBackground.setVolume(volume);

        breakClip.setVolume(audioClip);
        hitClip.setVolume(audioClip);
        explosionClip.setVolume(audioClip);
        soundLosing.setVolume(audioClip);
    }
}
