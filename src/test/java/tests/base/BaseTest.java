package tests.base;

import common.CommonActions;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import pages.base.BasePage;
import pages.blogPage.BlogPage;
import tests.common.TestListener;

import static common.Config.CLEAR_COOKIES_AND_STORAGE;

/**
 * Базовый класс для всех тестов.
 * Аннотация @Listeners подключает ваш листенер для Allure.
 */
@Listeners(TestListener.class)
public class BaseTest {
    private final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    // ThreadLocal обеспечивает независимость драйверов при параллельном запуске
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    protected BasePage basePage;
    protected BlogPage blogPage;

    @BeforeMethod
    public void setUp() {
        logger.info("--- Старт теста. Инициализация драйвера для потока: {} ---", Thread.currentThread().getName());
        WebDriver driver = CommonActions.createDriver();
        if (driver != null) {
            driverThread.set(driver);
        } else {
            logger.error("Критическая ошибка: WebDriver не инициализирован!");
            throw new IllegalStateException("Failed to create WebDriver instance.");
        }

        // Инициализация страниц
        basePage = new BasePage(getDriver());
        blogPage = new BlogPage(getDriver());
    }

    public WebDriver getDriver() {
        return driverThread.get();
    }

    /**
     * Метод для Allure, который прикрепляет скриншот к отчету.
     */
    @Attachment(value = "Скриншот при падении", type = "image/png")
    public byte[] saveScreenshot() {
        return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Завершение теста. ITestResult позволяет проверить, упал ли тест.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();
        if (driver != null) {
            try {
                // Если тест завершился с ошибкой — делаем скриншот НЕМЕДЛЕННО
                if (result.getStatus() == ITestResult.FAILURE) {
                    logger.error("Тест '{}' провален. Делаю скриншот...", result.getName());
                    saveScreenshot();
                }

                if (CLEAR_COOKIES_AND_STORAGE) {
                    JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
                    driver.manage().deleteAllCookies();
                    javascriptExecutor.executeScript("window.sessionStorage.clear()");
                    javascriptExecutor.executeScript("window.localStorage.clear()");
                    logger.info("Очистка куки и хранилища выполнена.");
                }
            } catch (Exception e) {
                logger.warn("Ошибка в блоке tearDown: {}", e.getMessage());
            } finally {
                // Закрываем браузер и чистим поток, чтобы избежать утечек памяти
                driver.quit();
                driverThread.remove();
                logger.info("--- Сессия браузера завершена ---");
            }
        }
    }
}
