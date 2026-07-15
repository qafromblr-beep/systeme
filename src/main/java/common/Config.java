package common;
public class Config {
    public static final String PLATFORM_AND_BROWSER = "win_chrome";
    public static final Boolean CLEAR_COOKIES_AND_STORAGE = true;
    public static final String TEST_URL = "https://systeme.io/blog/cost-of-online-course";
    public static final String DB_URL = System.getProperty("db.url", "jdbc:postgresql://localhost:5432/your_db");
    public static final String DB_USER = System.getProperty("db.user", "postgres");
    public static final String DB_PASSWORD = System.getProperty("db.password", "password");
}