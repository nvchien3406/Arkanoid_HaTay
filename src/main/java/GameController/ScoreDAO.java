package GameController;

import Models.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    public ScoreDAO() {
        // Tạo bảng nếu chưa có
        createTableIfNotExists();
    }

    // Tạo bảng nếu chưa có
    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS scores (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                player_name TEXT NOT NULL,
                score INTEGER NOT NULL,
                created_at TEXT NOT NULL
            );
            """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println(" Bảng 'scores' đã sẵn sàng!");
        } catch (SQLException e) {
            System.out.println(" Lỗi khi tạo bảng: " + e.getMessage());
        }
    }

    //  Ghi điểm mới
    public void insertScore(String playerName, int score) {
        String sql = "INSERT INTO scores (player_name, score, created_at) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, playerName);
            pstmt.setInt(2, score);
            pstmt.setString(3, LocalDateTime.now().toString());
            pstmt.executeUpdate();

            System.out.println(" Đã lưu điểm thành công!");
        } catch (SQLException e) {
            System.out.println(" Lỗi ghi điểm: " + e.getMessage());
        }
    }

    //  Lấy danh sách top 10 điểm cao
    public List<String> getHighScores() {
        String sql = "SELECT player_name, score, created_at FROM scores ORDER BY score DESC LIMIT 10";
        List<String> highScores = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {

            //System.out.println(" TOP 10 NGƯỜI CHƠI:");
            int cnt = 1;
            while (res.next()) {
                String line = cnt + ". " +  res.getString("player_name") + " - " + res.getInt("score");
                highScores.add(line);
                cnt++;
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi đọc điểm: " + e.getMessage());
        }

        return highScores;
    }

    public String getRankPlayer(Player player) {
        String sql = "SELECT COUNT(*) + 1 AS Rank FROM scores WHERE score > ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, player.getScore());
            ResultSet res = pstmt.executeQuery();
            if(res.next()) {
                return String.valueOf(res.getInt("Rank"));
            }
        } catch (SQLException e) {
            System.out.println(" Lỗi đọc rank: " + e.getMessage());
        }
        return "N/A";
    }
}

