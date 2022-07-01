package controlling.contractRegister.web;

import com.google.gson.Gson;
import controlling.contractRegister.dic.ProjectType;
import controlling.contractRegister.export.ProjectExportObject;
import controlling.contractRegister.model.Project;
import controlling.contractRegister.service.ExcelExporterService;
import controlling.contractRegister.service.ProjectService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ProjectRESTController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ExcelExporterService excelExporterService;

    @ResponseBody
    @RequestMapping(value = "/getProjectSymbolValidationResult/{year}/{symbol}", method = RequestMethod.GET)
    public String validateProjectSymbol(@PathVariable("year") double year,
                                        @PathVariable("symbol") String symbol) {
        boolean result = projectService.existsProjectByYearAndSymbol(year, symbol);
        JSONObject json = new JSONObject();
        json.put("result", Boolean.toString(!result));

        if (result) {
            json.put("message", String.format(messageSource.getMessage("add.project.modal.step2.content.project-symbol-validation-error", null, LocaleContextHolder.getLocale()), symbol));
        }

        return json.toString();
    }

    @ResponseBody
    @RequestMapping(value = {"/getParentProjectList/{year}/{type}", "/getParentProjectList/{year}/{type}/{id}"},
            method = RequestMethod.GET)
    public String filterParentProjects(@PathVariable("year") double year,
                                       @PathVariable("type") ProjectType projectType,
                                       @PathVariable(required = false) Integer id) {
        Gson gson = new Gson();
        int idToCompare = Optional.ofNullable(id).orElse(-1);

        List<Project> childProjects = projectService.getAllChildProjects(id);
        List<Project> parentProjects = projectService.getProjectsByYearAndProjectType(year, projectType);

        Map<Integer, String> projectIdToProjectHeader = parentProjects.stream()
                .filter(p -> !p.getId().equals(idToCompare))
                .filter(p -> !childProjects.contains(p))
                .collect(Collectors.toMap(Project::getId, Project::getProjectHeader));

        return gson.toJson(projectIdToProjectHeader);
    }

    @ResponseBody
    @RequestMapping(value = "/getFDMProjectList/{year}", method = RequestMethod.GET)
    public String fdmProjects(@PathVariable("year") double year) {
        Gson gson = new Gson();
        List<List<?>> fdmProjectsResponse = new ArrayList<>();

        List<Project> fdmProjects = projectService.getFDMProjects(year);
        List<Integer> fdmProjectsId = fdmProjects.stream().map(Project::getId).collect(Collectors.toList());
        List<String> fdmProjectsHeader = fdmProjects.stream().map(Project::getProjectHeader).collect(Collectors.toList());
        fdmProjectsResponse.add(fdmProjectsId);
        fdmProjectsResponse.add(fdmProjectsHeader);

        return gson.toJson(fdmProjectsResponse);
    }

    @GetMapping("/projects/export/excel")
    public void exportProjectsToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=projects_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Project> projects = projectService.getAllProjects();

        excelExporterService.export(new ProjectExportObject(projects), response.getOutputStream());
    }
}