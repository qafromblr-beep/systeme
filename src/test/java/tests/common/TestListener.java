package tests.common;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.base.BaseTest;

public class TestListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest baseTest) {
            // Просто вызываем метод, помеченный @Attachment
            if (baseTest.getDriver() != null) {
                baseTest.saveScreenshot();
            }
        }
    }
}
