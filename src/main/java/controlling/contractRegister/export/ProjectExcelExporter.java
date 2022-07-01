package controlling.contractRegister.export;

import controlling.contractRegister.model.Project;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class ProjectExcelExporter implements ExcelExporter<Project> {

    @Override
    public Class<Project> getObjectClass() {
        return Project.class;
    }

    @Override
    public String getSheetName() {
        return "projects.excel.sheet.name";
    }

    @Override
    public List<String> getHeaderNames() {
        return List.of(
                "projects.excel.sheet.header.id",
                "projects.excel.sheet.header.year",
                "projects.excel.sheet.header.symbol",
                "projects.excel.sheet.header.parent",
                "projects.excel.sheet.header.type",
                "projects.excel.sheet.header.name",
                "projects.excel.sheet.header.date-from",
                "projects.excel.sheet.header.date-to",
                "projects.excel.sheet.header.description"
        );
    }

    @Override
    public List<Pair<Object, CellStyle>> getCells(XSSFWorkbook workbook, Project project) {
        CellStyle style = ExcelCellStyleUtil.getDefaultCellStyle(workbook);
        CellStyle dateStyle = ExcelCellStyleUtil.getDateCellStyle(workbook);

        return List.of(
                Pair.create(project.getId(), style),
                Pair.create(project.getYear(), style),
                Pair.create(project.getSymbol(), style),
                Pair.create(getParentProject(project), style),
                Pair.create(project.getProjectType().getName(), style),
                Pair.create(project.getName(), style),
                Pair.create(project.getDateFrom(), dateStyle),
                Pair.create(getDateTo(project), dateStyle),
                Pair.create(getDescription(project), style)
        );
    }

    private String getParentProject(Project project) {
        return Optional.ofNullable(project.getParentProject()).map(Project::getProjectHeader).orElse("");
    }

    private String getDescription(Project project) {
        return Optional.ofNullable(project.getDescription()).orElse("");
    }

    private Date getDateTo(Project project) {
        return Optional.ofNullable(project.getDateTo()).orElse(Date.from(LocalDate.of(9999, 12, 31).atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));
    }
}