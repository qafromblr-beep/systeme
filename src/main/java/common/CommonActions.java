package common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

import static common.Config.PLATFORM_AND_BROWSER;
import static constants.Constant.IMPLICIT_WAIT;

public class CommonActions {
    public static WebDriver createDriver() {
        System.out.println("Инициализация WebDriver...");
        ChromeOptions options = new ChromeOptions();

        // 1. Настройка бинарного файла для Docker (Alpine)
        // Читаю путь из ENV CHROME_BIN, который указал в Dockerfile
        String chromeBin = System.getenv("CHROME_BIN");
        if (chromeBin != null && !chromeBin.isEmpty()) {
            options.setBinary(chromeBin);
        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            options.setBinary("/usr/bin/chromium-browser");
        }

        options.addArguments("--headless=new"); // Новый headless режим
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage"); // Критично для Docker
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
            // Проверка платформы (игнорируем регистр для надежности)
            if (PLATFORM_AND_BROWSER.toLowerCase().contains("chrome")) {
                driver = new ChromeDriver(options);
            } else {
                Assert.fail("Неподдерживаемая платформа или браузер: " + PLATFORM_AND_BROWSER);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при создании WebDriver: " + e.getMessage());
        }

        if (driver != null) {
            System.out.println("WebDriver успешно создан.");
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT));
        } else {
            System.err.println("WebDriver не был инициализирован. Возвращен null.");
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
