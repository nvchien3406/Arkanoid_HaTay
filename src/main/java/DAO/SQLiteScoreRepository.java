package DAO;

import Models.Player;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SQLiteScoreRepository implements IScoreRepository {
    private final DatabaseManager dbManager;

    public SQLiteScoreRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS scores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_name TEXT NOT NULL,
                score INTEGER NOT NULL,
                created_at TEXT NOT NULL
            );
        """;

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Lỗi tạo bảng: " + e.getMessage());
        }
    }

    @Override
    public void insertScore(String playerName, int score) {
        String sql = "INSERT INTO scores (player_name, score, created_at) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.setString(3, LocalDateTime.now().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Lỗi ghi điểm: " + e.getMessage());
        }
    }

    @Override
    public List<String> getHighScores() {
        List<String> highScores = new ArrayList<>();
        String sql = "SELECT player_name, score FROM scores ORDER BY score DESC LIMIT 10";

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            int rank = 1;
            while (res.next()) {
                highScores.add(rank + ". " + res.getString("player_name") + " - " + res.getInt("score"));
                rank++;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi đọc điểm: " + e.getMessage());
        }
        return highScores;
    }

    @Override
    public int getTopScore() {
        String sql = "SELECT MAX(score) AS max_score FROM scores";
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            if (res.next()) return res.getInt("max_score");
        } catch (SQLException e) {
            System.out.println("Lỗi đọc điểm cao nhất: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public String getRankPlayer(Player player) {
        String sql = "SELECT COUNT(*) + 1 AS rank FROM scores WHERE score > ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, player.getScore());
            ResultSet res = pstmt.executeQuery();
            if (res.next()) return String.valueOf(res.getInt("rank"));
        } catch (SQLException e) {
            System.out.println("Lỗi đọc rank: " + e.getMessage());
        }
        return "N/A";
    }

    @Override
    public List<Player> getTopPlayers() {
        List<Player> players = new ArrayList<>();
        String sql = """
            SELECT player_name, score,
            (SELECT COUNT(*) + 1 FROM scores s2 WHERE s2.score > s1.score) AS rank
            FROM scores s1 ORDER BY s1.score DESC LIMIT 10
        """;

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            while (res.next()) {
                players.add(new Player(
                        res.getString("player_name"),
                        res.getInt("rank"),
                        res.getInt("score"),
                        0
                ));
            }
        } catch (SQLException e) {
            System.out.println("Lỗi get top players: " + e.getMessage());
        }
        return players;
    }
}
