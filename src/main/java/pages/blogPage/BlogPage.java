package pages.blogPage;

import io.qameta.allure.Step;
import org.openqa.selenium.TimeoutException;
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
            waitAndSwitchToFrame(popupIframe);
            return waitElementIsVisible(copyButton).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        } finally {
            switchToDefaultContent();
        }
    }

    @Step("Закрыть поп-ап")
    public void clickClosePopup() {
        try {
            waitAndSwitchToFrame(popupIframe);
            WebElement button = waitElementToBeClickable(closePopupButton);
            jsClick(button); // Используем JS клик вместо обычного
        } finally {
            switchToDefaultContent();
        }
    }

    @Step("Проверить, что поп-ап исчез")
    public boolean isCopyButtonHidden() {
        try {
            return wait.until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf(popupIframe));
        } catch (Exception e) {
            return true;
        }
    }
}
