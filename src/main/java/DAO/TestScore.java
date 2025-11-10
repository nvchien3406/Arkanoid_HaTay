package DAO;

public class TestScore {
    public static void main(String[] args) {
        ScoreDAO dao = new ScoreDAO();
        dao.insertScore("Bảo", 1500);
        dao.insertScore("Nam", 900);
        dao.getHighScores();
    }
}
