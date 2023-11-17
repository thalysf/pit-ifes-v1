package br.com.ifes.backend_pit.reports.styles;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

public class Style {

    public static CellStyle getDefaultCellStyle(HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT); // Alinhamento horizontal do texto (LEFT)
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // Alinhamento vertical do texto (CENTER)
        style.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }

    public static CellStyle getDefaultAtividadesHeaderCellStyle(HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT); // Alinhamento horizontal do texto (LEFT)
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // Alinhamento vertical do texto (CENTER)
        style.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }

    public static CellStyle getCellStyleBoldAlighRight(HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT); // Alinhamento horizontal do texto (RIGHT)
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // Alinhamento vertical do texto (CENTER)
        style.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // Negrito
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }

    public static CellStyle getDefaultSubCellStyle(HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER); // Alinhamento horizontal do texto (CENTER)
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // Alinhamento vertical do texto (CENTER)
        style.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }

    public static CellStyle getDefaultLeftAlignSubCellStyle(HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_LEFT); // Alinhamento horizontal do texto (LEFT)
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // Alinhamento vertical do texto (CENTER)
        style.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }

    public static CellStyle getSubtotalParcialStyle(HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_RIGHT); // Alinhamento horizontal do texto (RIGHT)
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // Alinhamento vertical do texto (CENTER)
        style.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula

        return style;
    }

    public static CellStyle getBoldCellStyle(HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER); // Alinhamento horizontal do texto (CENTER)
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // Alinhamento vertical do texto (CENTER)
        style.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // Negrito
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }

    public static CellStyle getMainHeaderBoldCellStyle(HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER); // Alinhamento horizontal do texto (CENTER)
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // Alinhamento vertical do texto (CENTER)
        style.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // Negrito
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);

        return style;
    }

    public static CellStyle getSubMainHeaderBoldCellStyle(HSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER); // Alinhamento horizontal do texto (CENTER)
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER); // Alinhamento vertical do texto (CENTER)
        style.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula
        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); // Negrito
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        return style;
    }

    public static RichTextString formatRichText(String text, HSSFWorkbook workbook) {
        RichTextString richText = new HSSFRichTextString(text);

        Font boldFont = workbook.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // Negrito
        boldFont.setFontHeightInPoints((short) 10);

        Font italicFont = workbook.createFont();
        italicFont.setItalic(true); // Itálico
        italicFont.setFontHeightInPoints((short) 10);


        richText.applyFont(0, text.indexOf("("), boldFont);  // Aplica o estilo negrito para a palavra "Subtotal"
        richText.applyFont(text.indexOf("("), text.length(), italicFont);  // Aplica o estilo itálico para o restante
        return richText;
    }

    public static void applyBorders(Sheet sheet, Workbook workbook, CellRangeAddress range, BorderStyle borderTop, BorderStyle borderBottom, BorderStyle borderLeft, BorderStyle borderRight) {
        RegionUtil.setBorderTop(borderTop.ordinal(), range, sheet, workbook);
        RegionUtil.setBorderBottom(borderBottom.ordinal(), range, sheet, workbook);
        RegionUtil.setBorderLeft(borderLeft.ordinal(), range, sheet, workbook);
        RegionUtil.setBorderRight(borderRight.ordinal(), range, sheet, workbook);
    }

    public static void applyBackground(Workbook workbook, CellRangeAddress range, IndexedColors indexedColors) {
        Sheet sheet = workbook.getSheetAt(0);

        // Obtém o estilo das células existentes no intervalo
        CellStyle existingStyle = null;
        for (int row = range.getFirstRow(); row <= range.getLastRow(); row++) {
            Row currentRow = sheet.getRow(row);
            if (currentRow != null) {
                for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
                    Cell cell = currentRow.getCell(col);
                    if (cell != null) {
                        existingStyle = cell.getCellStyle();
                        break;
                    }
                }
                if (existingStyle != null) {
                    break;
                }
            }
        }

        // Se não há estilo existente, cria um novo estilo
        if (existingStyle == null) {
            existingStyle = workbook.createCellStyle();
        }

        // Cria uma cópia do estilo existente e modifica o background para cinza claro
        CellStyle newStyle = workbook.createCellStyle();
        newStyle.cloneStyleFrom(existingStyle);
        newStyle.setWrapText(true); // quebra de linha e virtude do tamanho de largura da célula
        newStyle.setFillPattern((short) FillPatternType.SOLID_FOREGROUND.ordinal());
        newStyle.setFillForegroundColor(indexedColors.getIndex());

        // Aplica o novo estilo às células no intervalo
        for (int row = range.getFirstRow(); row <= range.getLastRow(); row++) {
            Row currentRow = sheet.getRow(row);
            if (currentRow == null) {
                currentRow = sheet.createRow(row);
            }
            for (int col = range.getFirstColumn(); col <= range.getLastColumn(); col++) {
                Cell cell = currentRow.getCell(col);
                if (cell == null) {
                    cell = currentRow.createCell(col);
                }
                cell.setCellStyle(newStyle);
            }
        }
    }
}
