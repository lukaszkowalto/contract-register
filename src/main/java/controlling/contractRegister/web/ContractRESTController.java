package controlling.contractRegister.web;

import controlling.contractRegister.export.ContractExportObject;
import controlling.contractRegister.model.Contract;
import controlling.contractRegister.model.ContractAttachment;
import controlling.contractRegister.service.ContractAttachmentService;
import controlling.contractRegister.service.ContractService;
import controlling.contractRegister.service.ExcelExporterService;
import controlling.contractRegister.service.FileStorageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class ContractRESTController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractAttachmentService contractAttachmentService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ExcelExporterService excelExporterService;

    @Autowired
    private FileStorageService fileStorageService;

    @ResponseBody
    @RequestMapping(value = "/getContractNumberValidationResult/{contractNumber}", method = RequestMethod.GET)
    public String validateContractNumber(@PathVariable("contractNumber") String contractNumber) {
        String correctContractNumber = contractNumber.replace("_", "/");
        boolean result = contractService.existsContractByContractNumber(correctContractNumber);
        JSONObject json = new JSONObject();
        json.put("result", Boolean.toString(!result));

        if (result) {
            json.put("message", String.format(messageSource.getMessage("add.contract.modal.step1.content.contract-number-validation-error", null, LocaleContextHolder.getLocale()), correctContractNumber));
        }

        return json.toString();
    }

    @GetMapping("/contracts/export/excel")
    public void exportContractsToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=contracts_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Contract> contracts = contractService.getAllContracts();

        excelExporterService.export(new ContractExportObject(contracts), response.getOutputStream());
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> getFile(@RequestParam("id") Integer id) {
        ContractAttachment contractAttachment = contractAttachmentService.getContractAttachmentById(id);

        String filename = contractAttachment.getToken().toString() + "." + contractAttachment.getDocumentFormat();
        Resource file = fileStorageService.get(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment;filename=\"" + file.getFilename() + "\"").body(file);
    }
}