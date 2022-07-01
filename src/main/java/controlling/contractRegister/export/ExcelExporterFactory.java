package controlling.contractRegister.export;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExcelExporterFactory {

    @Autowired
    private List<ExcelExporter> excelExporters;

    public ExcelExporter getExcelExporter(Class<?> clazz) {
        return ListUtils.emptyIfNull(excelExporters).stream()
                .filter(exporter -> exporter.getObjectClass() == clazz)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Could not find exporter for class " + clazz));
    }
}