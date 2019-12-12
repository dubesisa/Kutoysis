package Kutoysis.utilities;

import Kutoysis.reporting.ExtentManager;
import Kutoysis.reporting.ExtentTestManager;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    public void onStart(ITestContext context) {
        System.out.println("*** Test Suite " + context.getName() + " started ***");
    }

    public void onFinish(ITestContext context) {
        System.out.println(("*** Test Suite " + context.getName() + " ending ***"));
        ExtentTestManager.endTest();
        ExtentManager.getInstance().flush();
    }

    public void onTestStart(ITestResult result) {
        System.out.println(("*** Running test method " + result.getTestContext().getName() + " - " + result.getTestContext().getAttribute("testName").toString() + "..."));

        ExtentTestManager.startTest(result.getTestContext().getName() + " - " + result.getTestContext().getAttribute("testName").toString());
    }

    public void onTestSuccess(ITestResult result) {
        System.out.println("*** Executed " + result.getTestContext().getName() + " - " + result.getTestContext().getAttribute("testName").toString() + " successfully...");
        ExtentTestManager.getTest().log(Status.PASS, result.getTestContext().getName() + " - " + result.getTestContext().getAttribute("testName").toString() + "Test passed");
    }

    public void onTestFailure(ITestResult result) {
        System.out.println("*** Test execution " + result.getTestContext().getName() + " - " + result.getTestContext().getAttribute("testName").toString() + " failed...");
        ExtentTestManager.getTest().log(Status.FAIL, result.getTestContext().getName() + " - " + result.getTestContext().getAttribute("testName").toString() + "Test Failed");
    }

    public void onTestSkipped(ITestResult result) {
        System.out.println("*** Test " + result.getTestContext().getName() + " - " + result.getTestContext().getAttribute("testName").toString() + " skipped...");
        ExtentTestManager.getTest().log(Status.SKIP, result.getTestContext().getName() + " - " + result.getTestContext().getAttribute("testName").toString() + "Test Skipped");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("*** Test failed but within percentage % " + result.getTestContext().getName() + " - " + result.getTestContext().getAttribute("testName").toString());
    }

}