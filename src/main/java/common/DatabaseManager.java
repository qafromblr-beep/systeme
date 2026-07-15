package common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;


import static common.Config.*;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // Пример метода для получения одного значения (например, email из таблицы users)
    public static String executeSelect(String query, String columnName) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getString(columnName);
            }
        } catch (SQLException e) {
            logger.error("Ошибка при выполнении SQL запроса: {}", e.getMessage());
        }
        return null;
    }

    // Метод для проверки существования записи (вернуло ли что-то SELECT)
    public static boolean isRecordPresent(String query) {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next();
        } catch (SQLException e) {
            logger.error("Ошибка проверки записи в БД: {}", e.getMessage());
            return false;
        }
    }
}
