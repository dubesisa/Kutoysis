package Kutoysis.reporting;

import Kutoysis.utilities.FilePathHandler;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.Platform;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {
    private static ExtentReports extent;
    private static String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
    private static String reportFileName = "Test-Report" + ".html";
    private static String fileSeparator = System.getProperty("file.separator");
    private static String reportFilepath = System.getProperty("user.dir") + fileSeparator + "TestReport";
    private static String reportFileLocation = reportFilepath + fileSeparator + reportFileName;


    public static ExtentReports getInstance() {
        if (extent == null)
            extent = createInstance();
        return extent;
    }

    //Create an extent report instance
    private static ExtentReports createInstance() {
        String fileName = getReportPath(reportFilepath);

        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle(reportFileName);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("<img src='.." + fileSeparator + "logo" + fileSeparator + "logo.png'");
        htmlReporter.config().setCSS(".report-name { padding-left: 0px; } .report-name > img { height: 60%;margin-left: 0px;margin-bottom: -10px;width: auto; }");
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
        htmlReporter.config().setAutoCreateRelativePathMedia(true);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        //Set environment details
        extent.setSystemInfo("OS", String.valueOf(Platform.getCurrent()));
        return extent;
    }

    //Create an extent report instance
    private static ExtentReports createInstanceCustomXml() {
        String fileName = getReportPath(reportFilepath);
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.loadXMLConfig(FilePathHandler.GetResourceFolderPath() + fileSeparator + "extent-config.xml");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        //Set environment details
        extent.setSystemInfo("OS", String.valueOf(Platform.getCurrent()));
        return extent;
    }

    //Create the report path
    private static String getReportPath(String path) {
        File testDirectory = new File(path);
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                System.out.println("Directory: " + path + " is created!");
                return reportFileLocation;
            } else {
                System.out.println("Failed to create directory: " + path);
                return System.getProperty("user.dir");
            }
        } else {
            System.out.println("Directory already exists: " + path);
        }
        return reportFileLocation;
    }

}