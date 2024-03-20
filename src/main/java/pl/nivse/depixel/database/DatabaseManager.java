package pl.nivse.depixel.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private HikariDataSource dataSource = new HikariDataSource();
    public DatabaseManager(String host, String username, String password) throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(host);
        config.setUsername(username);
        config.setPassword(password);
        dataSource = new HikariDataSource(config);
    }
    
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
