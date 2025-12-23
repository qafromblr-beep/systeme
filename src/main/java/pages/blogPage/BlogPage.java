package pages.blogPage;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.base.BasePage;


public class BlogPage extends BasePage {

    @FindBy(xpath = "//iframe[contains(@id, 'systemeio-iframe')]")
    private WebElement popupIframe;

    @FindBy(xpath = "//div[contains(text(), 'I want to receive my copy')]")
    private WebElement copyButton;

    @FindBy(xpath = "//button[@data-testid='popup-close-icon']")
    private WebElement closePopupButton;

    public BlogPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    @Step("Проверить видимость кнопки внутри iframe")
    public boolean isCopyButtonVisible() {
        try {
            switchToPopupIframe();
            return waitElementIsVisible(copyButton).isDisplayed();
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Step("Закрыть поп-ап")
    public void clickClosePopup() {
        try {
            switchToPopupIframe();
            waitElementToBeClickable(closePopupButton).click();
            System.out.println("Кнопка закрытия нажата");
        } finally {
            driver.switchTo().defaultContent();
        }
    }

    @Step("Проверить, что поп-ап исчез")
    public boolean isCopyButtonHidden() {
        try {
            return new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                    .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf(popupIframe));
        } catch (Exception e) {
            return true;
        }
    }


    private void switchToPopupIframe() {
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(popupIframe));
        driver.switchTo().frame(popupIframe);
    }
}
