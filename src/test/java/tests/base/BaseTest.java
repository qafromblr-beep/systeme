package tests.base;

import common.CommonActions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.base.BasePage;
import pages.blogPage.BlogPage;
import static common.Config.CLEAR_COOKIES_AND_STORAGE;

public class BaseTest {
    // Динамический логгер: подхватит имя наследника (например, SystemeTest)
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    protected BasePage basePage;
    protected BlogPage blogPage;

    @BeforeMethod
    public void setUp() {
        logger.info("Инициализация драйвера для потока: {}", Thread.currentThread().getName());
        WebDriver driver = CommonActions.createDriver();
        if (driver != null) {
            driverThread.set(driver);
        } else {
            logger.error("Не удалось создать экземпляр WebDriver!");
            throw new IllegalStateException("Failed to create WebDriver instance.");
        }
        basePage = new BasePage(getDriver());
        blogPage = new BlogPage(getDriver());
    }

    public WebDriver getDriver() {
        return driverThread.get();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                if (CLEAR_COOKIES_AND_STORAGE) {
                    JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
                    driver.manage().deleteAllCookies();
                    javascriptExecutor.executeScript("window.sessionStorage.clear()");
                    javascriptExecutor.executeScript("window.localStorage.clear()");
                    logger.info("Куки и локальное хранилище успешно очищены.");
                }
            } catch (Exception e) {
                logger.warn("Ошибка при очистке куков или хранилища: {}", e.getMessage());
            } finally {
                driver.quit();
                driverThread.remove();
                logger.info("Сессия браузера завершена и драйвер удален.");
            }
        }
    }
}
