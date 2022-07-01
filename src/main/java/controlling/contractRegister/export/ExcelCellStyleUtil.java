package controlling.contractRegister.export;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelCellStyleUtil {

    public static CellStyle getHeaderCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = getFont(workbook, 16);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    public static CellStyle getDefaultCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = getFont(workbook, 14);
        style.setFont(font);
        return style;
    }

    public static CellStyle getDateCellStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = getFont(workbook, 14);
        style.setFont(font);
        style.setDataFormat((short) 14);
        return style;
    }

    private static XSSFFont getFont(XSSFWorkbook workbook, int height) {
        XSSFFont font = workbook.createFont();
        font.setFontHeight(height);
        return font;
    }
}