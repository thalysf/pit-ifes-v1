package br.com.ifes.backend_pit.reports.util;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class Util {
    public static void adjustColumnWidths(HSSFSheet sheet, int numberOfColumns) {
        for (int col = 1; col < numberOfColumns; col++) {
            sheet.setColumnWidth(col, 15 * 256);
        }
    }
    public static int getNextRowToFill(HSSFSheet sheet, int column) {
        int lastRow = sheet.getLastRowNum();
        while (lastRow >= 0) {
            Row row = sheet.getRow(lastRow);
            if (row != null) {
                Cell cell = row.getCell(column);
                if (cell != null && !cell.getStringCellValue().isEmpty()) {
                    break; // Encontrou a última célula preenchida
                }
            }
            lastRow--;
        }
        return lastRow + 2; // Retorna a próxima linha após a última preenchida
    }
}
