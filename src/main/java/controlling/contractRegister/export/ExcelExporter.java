package controlling.contractRegister.export;

import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface ExcelExporter<T> {

    Class<T> getObjectClass();

    String getSheetName();

    List<String> getHeaderNames();

    List<Pair<Object, CellStyle>> getCells(XSSFWorkbook workbook, T object);
}