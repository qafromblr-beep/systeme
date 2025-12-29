package tests.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import tests.base.BaseTest;

public class TestListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {
        Object testClass = result.getInstance();

        // Используем Pattern Matching (доступно в Java 17)
        if (testClass instanceof BaseTest baseTest) {
            // Теперь не нужно писать: BaseTest baseTest = (BaseTest) testClass;

            baseTest.saveScreenshot();
            logger.error("Тест [{}] ПРОВАЛЕН. Скриншот прикреплен к Allure-отчету.", result.getName());

            if (result.getThrowable() != null) {
                logger.error("Причина падения: {}", result.getThrowable().getMessage());
            }
        }
    }
}
