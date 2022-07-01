package controlling.contractRegister.export;

import controlling.contractRegister.model.Category;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryExcelExporter implements ExcelExporter<Category> {

    @Override
    public Class<Category> getObjectClass() {
        return Category.class;
    }

    @Override
    public String getSheetName() {
        return "categories.excel.sheet.name";
    }

    @Override
    public List<String> getHeaderNames() {
        return List.of(
                "categories.excel.sheet.header.id",
                "categories.excel.sheet.header.year",
                "categories.excel.sheet.header.symbol",
                "categories.excel.sheet.header.parent",
                "categories.excel.sheet.header.type",
                "categories.excel.sheet.header.name",
                "categories.excel.sheet.header.description"
        );
    }

    @Override
    public List<Pair<Object, CellStyle>> getCells(XSSFWorkbook workbook, Category category) {
        CellStyle style = ExcelCellStyleUtil.getDefaultCellStyle(workbook);

        return List.of(
                Pair.create(category.getId(), style),
                Pair.create(category.getYear(), style),
                Pair.create(category.getSymbol(), style),
                Pair.create(getParentCategory(category), style),
                Pair.create(category.getCategoryType().getName(), style),
                Pair.create(category.getName(), style),
                Pair.create(getDescription(category), style)
        );
    }

    private String getParentCategory(Category category) {
        return Optional.ofNullable(category.getParentCategory()).map(Category::getCategoryHeader).orElse("");
    }

    private String getDescription(Category category) {
        return Optional.ofNullable(category.getDescription()).orElse("");
    }
}