package Models;

public class Player {
    private String playerName;
    private int score;
    private int lives;

    public Player(String playerName, int score , int lives) {
        this.playerName = playerName;
        this.score = score;
        this.lives = lives;
    }

    public String getPlayerName() { return playerName; }
    public int getScore() { return score; }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getLives() {
        return lives;
    }
    public void setLives(int lives) {
        this.lives = lives;
    }

    public boolean playerIsAlive() {
        return lives > 0;
    }
}
