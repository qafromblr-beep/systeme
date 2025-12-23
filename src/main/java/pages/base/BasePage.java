package pages.base;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static constants.Constant.EXPLICIT_WAIT;
public class BasePage {
    protected WebDriver driver;
    public BasePage(WebDriver driver) {
        this.driver = driver;
     }
    public WebElement waitElementIsVisible(WebElement element) {
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.visibilityOf(element));
    }
    public WebElement waitElementToBeClickable(WebElement element) {
        return new WebDriverWait(driver, Duration.ofSeconds(EXPLICIT_WAIT))
                .until(ExpectedConditions.elementToBeClickable(element));
    }
}
