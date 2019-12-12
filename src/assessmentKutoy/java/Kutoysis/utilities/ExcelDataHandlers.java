package Kutoysis.utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;

public class ExcelDataHandlers {
    private static String resourceFolderPath = FilePathHandler.GetResourceFolderPath();
    private DataFormatter dataFormatter = new DataFormatter();

    public Sheet GetExcelSheet(String workbookName, String sheetName) throws Exception {
        File excelFile = new File(resourceFolderPath + workbookName);
        FileInputStream fileInputStream = new FileInputStream(excelFile);
        Workbook workbook = WorkbookFactory.create(fileInputStream);
        fileInputStream.close();
        return workbook.getSheet(sheetName);
    }

    public Sheet GetExcelSheetFromWorkbook(Workbook workbook, String sheetName) {
        return workbook.getSheet(sheetName);
    }

    public Workbook GetExcelWorkbook(String workbookName) throws Exception {
        File ExcelFile = new File(resourceFolderPath + workbookName);
        FileInputStream fileInputStream = new FileInputStream(ExcelFile);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        fileInputStream.close();
        return workbook;
    }

    public String GetCellData(String columnName, int iRow, Sheet sheet) {
        String cellData = null;
        Row row = sheet.getRow(0);
        int rowCellCount = row.getLastCellNum();
        for (int i = 0; i <= rowCellCount; i++) {
            if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                Row raw = sheet.getRow(iRow);
                Cell cell = raw.getCell(i);
                cellData = dataFormatter.formatCellValue(cell);
                break;
            }
        }
        return cellData;
    }

    public Workbook SetCellData(String columnName, int iRow, String sheetName, String inputData, Workbook workbook) {
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(0);
        int rowCellCount = row.getLastCellNum();
        Cell cell = null;
        Row raw;
        for (int i = 0; i <= rowCellCount; i++) {
            if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                raw = sheet.getRow(iRow);
                cell = raw.getCell(i);
                break;
            }
        }
        //Update the value of cell
        cell.setCellValue(inputData);
        return workbook;
    }
}
