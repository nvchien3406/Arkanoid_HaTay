package DAO;

import Models.Player.Player;
import java.util.List;

public interface IScoreRepository {
    void insertScore(String playerName, int score);
    List<String> getHighScores();
    int getTopScore();
    String getRankPlayer(Player player);
    List<Player> getTopPlayers();
}
