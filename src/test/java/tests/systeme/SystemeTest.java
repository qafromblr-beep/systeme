package tests.systeme;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.blogPage.BlogPage;
import tests.base.BaseTest;
import static common.CommonActions.openPage;
public class SystemeTest extends BaseTest {
    @Test(description = "Проверка появления и закрытия маркетингового поп-апа")
    public void checkSystemePopup() {
        openPage(getDriver());
        BlogPage blogPage = new BlogPage(getDriver());
        Assert.assertTrue(blogPage.isCopyButtonVisible(), "Кнопка 'I want to receive my copy' не появилась!");
        blogPage.clickClosePopup();
        Assert.assertTrue(blogPage.isCopyButtonHidden(), "Поп-ап не закрылся после нажатия на крестик!");
    }
}
