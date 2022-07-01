package controlling.contractRegister.service;

import controlling.contractRegister.export.ExcelCellStyleUtil;
import controlling.contractRegister.export.ExcelExporter;
import controlling.contractRegister.export.ExcelExporterFactory;
import controlling.contractRegister.export.ExportObject;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ExcelExporterServiceImplementation implements ExcelExporterService {

    @Autowired
    private ExcelExporterFactory excelExporterFactory;

    @Autowired
    private MessageSource messageSource;

    @Override
    public <T> void export(ExportObject<T> exportObject, OutputStream outputStream) throws IOException {
        ExcelExporter<T> excelExporter = excelExporterFactory.getExcelExporter(exportObject.getObjectClass());

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(messageSource.getMessage(excelExporter.getSheetName(), null, LocaleContextHolder.getLocale()));

        writeHeaderLine(excelExporter, workbook, sheet);
        writeDataLines(excelExporter, exportObject.getObjects(), workbook, sheet);

        workbook.write(outputStream);
        workbook.close();

        outputStream.close();
    }

    private <T> void writeDataLines(ExcelExporter<T> excelExporter, List<T> objects, XSSFWorkbook workbook, XSSFSheet sheet) {
        int rowCount = 1;
        for (T object : objects) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (Pair<Object, CellStyle> cell : excelExporter.getCells(workbook, object)) {
                createCell(sheet, row, columnCount++, cell.getKey(), cell.getValue());
            }
        }
    }

    private <T> void writeHeaderLine(ExcelExporter<T> excelExporter, XSSFWorkbook workbook, XSSFSheet sheet) {
        Row row = sheet.createRow(0);
        List<String> headerNames = excelExporter.getHeaderNames();
        CellStyle style = ExcelCellStyleUtil.getHeaderCellStyle(workbook);

        IntStream.range(0, headerNames.size())
                .boxed()
                .forEach(index ->
                        createCell(sheet, row, index, messageSource.getMessage(headerNames.get(index), null, LocaleContextHolder.getLocale()), style));
    }

    private void createCell(XSSFSheet sheet, Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else {
            cell.setCellValue((double) value);
        }
        cell.setCellStyle(style);
    }
}