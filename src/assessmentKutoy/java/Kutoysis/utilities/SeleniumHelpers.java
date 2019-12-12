package Kutoysis.utilities;

import com.epam.reportportal.service.ReportPortal;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class SeleniumHelpers {
    private String screenshotsPath;
    private String fileSeparator;

    public By DetermineWebElementType(String webElementType, String elementId) {
        By loc = null;
        switch (webElementType.toUpperCase()) {
            case "ID":
                loc = By.id(elementId);
                break;
            case "NAME":
                loc = By.name(elementId);
                break;
            case "XPATH":
                loc = By.xpath(elementId);
                break;
            case "CSSSELECTOR":
                loc = By.cssSelector(elementId);
                break;
            case "CLASSNAME":
                loc = By.className(elementId);
                break;
            case "LINK TEXT":
                loc = By.linkText(elementId);
                break;
        }
        return loc;
    }

    public String logWebScreenShotToFile(WebDriver driver) throws IOException {

        String targetLocation;
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String screenShotName = Thread.currentThread().getName() + " - " + timeStamp + ".png";
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        targetLocation = screenshotsPath + fileSeparator + screenShotName;// define location

        File targetFile = new File(targetLocation);
        FileHandler.copy(screenshotFile, targetFile);
        return targetFile.getAbsolutePath();
    }

    public String logWebScreenShotToFileCompressed(WebDriver driver) throws IOException {

        String targetLocation;
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        String screenShotName = Thread.currentThread().getName() + " - " + timeStamp + ".png";
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        targetLocation = screenshotsPath + fileSeparator + screenShotName;// define location
        File compressedScreenshot = CompressCapturedScreenshot(screenshotFile, targetLocation);
        return compressedScreenshot.getAbsolutePath();
    }

    private File CompressCapturedScreenshot(File screenshotFile, String targetLocation) throws IOException {
        File input = new File(screenshotFile.getAbsolutePath());
        BufferedImage image = ImageIO.read(input);

        File compressedImageFile = new File(targetLocation);
        OutputStream os = new FileOutputStream(compressedImageFile);

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
        ImageWriter writer = writers.next();

        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();
        // Check if canWriteCompressed is true
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.05f);
        }
        // End of check
        writer.write(null, new IIOImage(image, null, null), param);
        return compressedImageFile;
    }

    public String logWebScreenShotToBase64(WebDriver driver) {
        /*Embeds into html but becomes too large*/
        String src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        return "data:image/jpg;base64, " + src;
    }

    public void SetupScreenshotsFolder() {
        fileSeparator = System.getProperty("file.separator");
        screenshotsPath = System.getProperty("user.dir") + fileSeparator + "screenshots";

        File file = new File(screenshotsPath); // Set screenshot folder
        if (!file.exists()) {
            if (file.mkdirs()) {
                System.out.println("Directory: " + file.getAbsolutePath() + " is created!");
            } else {
                System.out.println("Failed to create directory: " + file.getAbsolutePath());
            }

        }
    }

    public void LogScreenshotToRP(String stepName, String level, String screenshotPath) {
        File filePath = new File(screenshotPath);
        ReportPortal.emitLog(stepName, level, Calendar.getInstance().getTime(), filePath);
    }

    public Boolean ParseUrl(String dataToUse) {
        try {
            URL url = new URL(dataToUse);
            String protocol = url.getProtocol();
            String host = url.getHost();
            String path = url.getPath();
            return true;
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage() + " in provided url: " + dataToUse);
            return false;
        }
    }
}
