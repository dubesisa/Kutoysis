package Kutoysis.web;

import Kutoysis.models.TestStepsModel;
import Kutoysis.utilities.SeleniumHelpers;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;


public class WebFlowExecution {

    private SeleniumHelpers seleniumHelpers;

    public WebFlowExecution() {
        seleniumHelpers = new SeleniumHelpers();
        seleniumHelpers.SetupScreenshotsFolder();
    }

    public void WebStepExecutor(WebDriver driver, ExtentTest extentTestCase, TestStepsModel testData, SoftAssert assertion) throws IOException {
        String description = testData.Description;
        String action = testData.Action;
        String webElementType = testData.WebElementType;
        String webElementIdentifier = testData.WebElementIdentifier;
        String dataToUse = testData.DataToUse;
        JavascriptExecutor javaExecutor = (JavascriptExecutor) driver;
        By webelementToUse;
        ExtentTest stepNode = extentTestCase.createNode(description); // level = 2
        Duration interval = Duration.ofSeconds(1);
        Duration timeout = Duration.ofSeconds(10);
        Wait<WebDriver> verificationWait = new FluentWait<>(driver).withTimeout(timeout)
                .ignoring(NoSuchElementException.class).pollingEvery(interval);
        checkPageIsReady(javaExecutor);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        try {
            switch (action.toUpperCase()) {
                case "NAVIGATE TO PAGE":
                    Boolean validUrl = seleniumHelpers.ParseUrl(dataToUse);
                    if (validUrl) {
                        driver.navigate().to(dataToUse);
                    } else {
                        String combinedUrl;
                        if (dataToUse != null) {
                            combinedUrl = TestBase.baseUrl + dataToUse.trim().toLowerCase();
                        } else {
                            combinedUrl = TestBase.baseUrl;
                        }
                        validUrl = seleniumHelpers.ParseUrl(combinedUrl);
                        if (validUrl) {
                            driver.navigate().to(combinedUrl);
                        } else {
                            assertion.fail("Invalid url used: " + combinedUrl);
                        }
                    }
                    break;

                case "CLICK ELEMENT":
                    webelementToUse = seleniumHelpers.DetermineWebElementType(webElementType, webElementIdentifier);

                    try {
                        driver.findElement(webelementToUse).click();
                    } catch (ElementClickInterceptedException e) {
                        javaExecutor.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(webelementToUse));
                        driver.findElement(webelementToUse).click();
                    }
                    break;

                case "INPUT TEXT":
                    webelementToUse = seleniumHelpers.DetermineWebElementType(webElementType, webElementIdentifier);
                        driver.findElement(webelementToUse);
                    driver.findElement(webelementToUse).clear();

                        driver.findElement(webelementToUse).sendKeys(dataToUse);
                    break;

                case "VERIFY ELEMENT PRESENT":
                    webelementToUse = seleniumHelpers.DetermineWebElementType(webElementType, webElementIdentifier);
                    verificationWait.until(ExpectedConditions.visibilityOfElementLocated(webelementToUse)).isDisplayed();
                    break;


                case "VERIFY ELEMENT TEXT":
                    webelementToUse = seleniumHelpers.DetermineWebElementType(webElementType, webElementIdentifier);
                    verificationWait.until(ExpectedConditions.textToBePresentInElementLocated(webelementToUse, dataToUse));
                    break;

                case "WAIT":
                    int seconds = Integer.parseInt(dataToUse);
                    Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
                    break;


            }
            Thread.sleep(2000);
            String screenshotPath = seleniumHelpers.logWebScreenShotToFile(driver);
            assert screenshotPath != null;
            stepNode.pass(description, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            seleniumHelpers.LogScreenshotToRP(description, "INFO", screenshotPath);

        } catch (Exception e) {
            assertion.fail(e.getMessage());
            String screenshotPath = seleniumHelpers.logWebScreenShotToFile(driver);
            assert screenshotPath != null;
            stepNode.fail(description + " - " + e, MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            seleniumHelpers.LogScreenshotToRP(description + " - " + e, "ERROR", screenshotPath);
        }
    }

    public void checkPageIsReady(JavascriptExecutor javaExecutor) {
        if (javaExecutor.executeScript("return document.readyState").toString().equals("complete")) {
            return;
        }
        for (int i = 0; i < 25; i++) {

            //To check page ready state.
            if (javaExecutor.executeScript("return document.readyState").toString().equals("complete")) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
