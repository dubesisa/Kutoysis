package Kutoysis.utilities;

import org.openqa.selenium.Platform;


public class FilePathHandler {
    private static final String currentDir = System.getProperty("user.dir");
    public static String fileSeparator = System.getProperty("file.separator");
    private static String resourceFolder;

    public static String GetResourceFolderPath() {
        //MAC or Windows Selection for excel path
        if (Platform.getCurrent().toString().equalsIgnoreCase("MAC")) {
            resourceFolder = currentDir + "//src//test//resources//";
        } else {
            resourceFolder = currentDir + "\\src\\test\\resources\\";
        }
        return resourceFolder;
    }
}
