package GameController;

public interface ISoundService {
    void playBackground();
    void stopBackground();
    void pauseSoundBackground();
    void resumeSoundBackground();
    void playHit();
    void playBreak();
    void playExplosion();
    void playLosing();
    void setMasterVolume(double volume , double audioClip);
}
