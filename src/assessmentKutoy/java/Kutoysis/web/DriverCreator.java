package Kutoysis.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class DriverCreator {
    public static int implicitWait = 15;

    private WebDriver initChromeDriver() {
        System.out.println("Launching google chrome with new profile..");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    public WebDriver setDriver(String browserType, String appURL) {
        WebDriver driver = null;
        if ("chrome".equalsIgnoreCase(browserType)) {
            driver = initChromeDriver();
        } else {
            System.out.println("browser : " + browserType + " is invalid");
        }
        driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.navigate().to(appURL);
        return driver;
    }
}
