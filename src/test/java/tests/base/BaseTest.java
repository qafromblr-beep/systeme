package tests.base;

import common.CommonActions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.base.BasePage;
import pages.blogPage.BlogPage;
import static common.Config.CLEAR_COOKIES_AND_STORAGE;

public class BaseTest {
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
    protected BasePage basePage;
    protected BlogPage blogPage;

    @BeforeMethod
    public void setUp() {
        WebDriver driver = CommonActions.createDriver();
        if (driver != null) {
            driverThread.set(driver);
        } else {
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
                    System.out.println("Куки и хранилище очищены.");
                }
            } catch (Exception e) {
                System.err.println("Ошибка при очистке данных: " + e.getMessage());
            } finally {
                driver.quit();
                driverThread.remove();
                System.out.println("Браузер закрыт.");
            }
        }
    }
}
