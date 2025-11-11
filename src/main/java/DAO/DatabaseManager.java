package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class DatabaseManager {
    private final String dbPath;
    private final String url;

    public DatabaseManager(String dbPath) {
        this.dbPath = dbPath;
        this.url = "jdbc:sqlite:" + dbPath;
        createDataFolderIfNotExists();
    }

    private void createDataFolderIfNotExists() {
        File dataDir = new File(new File(dbPath).getParent());
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}

