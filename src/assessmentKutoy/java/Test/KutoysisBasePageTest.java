package Test;

import Kutoysis.models.TestStepsModel;
import Kutoysis.reporting.ExtentTestManager;
import Kutoysis.web.TestBase;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;


public class KutoysisBasePageTest extends TestBase implements ITest {

    private String testCaseName;
    private String newTestName;
    private SoftAssert assertion;

    @DataProvider(name = "WebFlowsFromExcel")
    public Iterator<Object[]> createArrayMapFromExcel() {
        return testCaseArray.entrySet().stream().map(input -> new Object[]{input.getKey(), input.getValue()})
                .iterator();
    }

    @BeforeMethod
    public void BeforeMethod(Method method, Object[] testData, ITestContext ctx) {
        if (testData.length > 0) {
            newTestName = (String) testData[0];
            ctx.setAttribute("testName", testData[0]);
        } else
            ctx.setAttribute("testName", method.getName());
    }

    @Test(dataProvider = "WebFlowsFromExcel")
    public void WebsiteStepsExecutor(String testFlow, ArrayList<TestStepsModel> testSteps) throws IOException {

        ExtentTest extentTestCase = ExtentTestManager.getTest();
        String platformName = GetPlatformFromTestFlow(extentTestCase.getModel().getName());
        assertion = new SoftAssert();
        for (TestStepsModel testStep : testSteps) {
            webFlowExecution.WebStepExecutor(driver, extentTestCase, testStep, assertion);
        }
        assertion.assertAll();
    }

    private String GetPlatformFromTestFlow(String testFlow) {
        int iend = testFlow.indexOf("-"); //this finds the first occurrence of "-"
        String subString = null;
        if (iend != -1) {
            subString = testFlow.substring(0, iend - 1); //platform name
        }
        return subString;
    }

    @Override
    public String getTestName() {
        this.testCaseName = newTestName;
        return this.testCaseName;
    }
}
