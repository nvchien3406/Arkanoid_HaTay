package GameController.Manager;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager implements ISoundService {
    private  MediaPlayer soundBackground ;

    private  AudioClip breakClip;
    private  AudioClip hitClip;
    private  AudioClip explosionClip;
    private   AudioClip soundLosing;
    //private  static AudioClip mouseClick;

    public SoundManager() {
        LoadSound();
    }


    public  void LoadSound() {
        try {
            //Sound BackGround
            String path = SoundManager.class.getResource("/sounds/S31-Night-Prowler.mp3").toExternalForm();
            Media media = new Media(path);
            soundBackground = new MediaPlayer(media);

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

    @Override
    public void playBackground() {
        try {
            soundBackground.setCycleCount(MediaPlayer.INDEFINITE);
            soundBackground.play();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stopBackground() {
        if(soundBackground != null) {
            soundBackground.stop();
            System.out.println("đã dừng nhạc nền");
        }
    }

    @Override
    public void pauseSoundBackground() {
        if(soundBackground != null) {
            soundBackground.pause();
        }
    }

    @Override
    public void resumeSoundBackground() {
        if(soundBackground != null) {
            soundBackground.play();
        }
    }

    @Override
    public void playHit() {
        if(hitClip != null) {
            hitClip.play();
        }
    }

    @Override
    public void playBreak() {
        if(breakClip != null) {
            breakClip.play();
        }
    }

    @Override
    public void playExplosion() {
        if(explosionClip  != null) {
            explosionClip.play();
        }
    }

    @Override
    public void playLosing(){
        if(soundLosing != null) {
            soundLosing.play();
        }
    }

//    public static void PlayMouseClick(){
//        if(mouseClick != null) {
//            mouseClick.play();
//        }
//    }

    @Override
    public void setMasterVolume(double volume , double audioClip) {
        soundBackground.setVolume(volume);

        breakClip.setVolume(audioClip);
        hitClip.setVolume(audioClip);
        explosionClip.setVolume(audioClip);
        soundLosing.setVolume(audioClip);
    }
}
