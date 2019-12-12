package Kutoysis.web;

import Kutoysis.models.TestStepsModel;
import Kutoysis.utilities.TestDataCreator;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TestBase {

    public HashMap<String, ArrayList<TestStepsModel>> testCaseArray;
    public WebDriver driver;
    public static String baseUrl;
    public WebFlowExecution webFlowExecution;
    public static Workbook cardWorkbook;


    @Parameters({"excelData", "appURL", "browserType", "connectedDevice",  "platformVersion", })
    @BeforeClass
    public void initializeTestBaseSetup(String excelData, String appURL, @Optional String browserType,
                                        @Optional String platformVersion) {
        try {
            testCaseArray = new TestDataCreator().CreateArrayFromExcel(excelData, "WebFlowsController");
            webFlowExecution = new WebFlowExecution();
            baseUrl = appURL;
            DriverCreator driverCreator = new DriverCreator();
                this.driver = driverCreator.setDriver(browserType, appURL);
        } catch (Exception e) {
            System.out.println("Error....." + Arrays.toString(e.getStackTrace()));
        }
    }
    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }

}