package tests.base;

import common.CommonActions;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import pages.base.BasePage;
import pages.blogPage.BlogPage;
import tests.common.TestListener;

import static common.Config.CLEAR_COOKIES_AND_STORAGE;

// Подключаем листенер на уровне базового класса, чтобы он работал во всех тестах
@Listeners(TestListener.class)
public class BaseTest {
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

    /**
     * Метод делает скриншот. В headless режиме Selenium берет данные из рендера,
     * поэтому скриншот будет рабочим.
     */
    @Attachment(value = "Screenshot on failure", type = "image/png")
    public byte[] saveScreenshot() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
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
                    logger.info("Куки и локальное хранилище очищены.");
                }
            } catch (Exception e) {
                logger.warn("Ошибка при очистке: {}", e.getMessage());
            } finally {
                driver.quit();
                driverThread.remove();
                logger.info("Сессия браузера завершена.");
            }
        }
    }
}
