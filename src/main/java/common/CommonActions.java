package common;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

import static common.Config.PLATFORM_AND_BROWSER;
import static constants.Constant.IMPLICIT_WAIT;

public class CommonActions {
    private static final Logger logger = LoggerFactory.getLogger(CommonActions.class);

    public static WebDriver createDriver() {
        logger.info("Инициализация WebDriver...");
        ChromeOptions options = new ChromeOptions();

        // 1. Настройка бинарного файла для Docker (Alpine)
        String chromeBin = System.getenv("CHROME_BIN");
        if (chromeBin != null && !chromeBin.isEmpty()) {
            options.setBinary(chromeBin);
            logger.info("Используется бинарный файл Chrome из ENV: {}", chromeBin);
        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            options.setBinary("/usr/bin/chromium-browser");
        }

        // 2. Инициализация WebDriverManager
        // Если мы не в Docker (бинарник не задан), WDM скачает нужный драйвер сам
        if (chromeBin == null || chromeBin.isEmpty()) {
            WebDriverManager.chromedriver().setup();
        }

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1200");
        options.addArguments("--incognito");

        // Маскировка автоматизации
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // Уникальная папка профиля для параллельного запуска
        String tmpDir = System.getProperty("java.io.tmpdir");
        options.addArguments("--user-data-dir=" + tmpDir + "selenium-profiles/profile-" + System.nanoTime());

        WebDriver driver = null;
        try {
            if (PLATFORM_AND_BROWSER.toLowerCase().contains("chrome")) {
                driver = new ChromeDriver(options);
            } else {
                Assert.fail("Неподдерживаемая платформа или браузер: " + PLATFORM_AND_BROWSER);
            }
        } catch (Exception e) {
            logger.error("Ошибка при создании WebDriver: {}", e.getMessage());
        }

        if (driver != null) {
            logger.info("WebDriver успешно создан.");
            // Напоминание: лучше установить IMPLICIT_WAIT = 0 в Constants
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        } else {
            logger.error("WebDriver не был инициализирован. Возвращен null.");
        }
        return driver;
    }

    public static void openPage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalStateException("WebDriver не инициализирован (null).");
        }
        driver.get(Config.TEST_URL);
    }
}
