package controlling.contractRegister.service;

import controlling.contractRegister.export.ExportObject;

import java.io.IOException;
import java.io.OutputStream;

public interface ExcelExporterService {

    <T> void export(ExportObject<T> exportObject, OutputStream outputStream) throws IOException;
}