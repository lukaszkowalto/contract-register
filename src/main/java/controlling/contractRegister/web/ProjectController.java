package controlling.contractRegister.web;

import controlling.contractRegister.pagination.PageSize;
import controlling.contractRegister.service.ProjectService;
import controlling.contractRegister.util.DateUtil;
import controlling.contractRegister.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/project")
    public String saveProject(@ModelAttribute("projectForm") @Valid ProjectDTO projectDTO, String callingInterface, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("ERROR: " +
                    bindingResult.getAllErrors().stream().map(ObjectError::getObjectName).collect(Collectors.joining(",")));
        }
        projectService.save(projectDTO);
        return "redirect:/" + callingInterface;
    }

    @GetMapping(value = "/projects")
    public String listProjects(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                               @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("years", DateUtil.getAvailableYears());
        model.addAttribute("projects", projectService.getPage(pageNumber, pageSize));
        model.addAttribute("projectForm", new ProjectDTO());
        model.addAttribute("callingInterface", "projects");
        return "select-project";
    }

    @PostMapping(value = "/projects")
    public String filterProjects(@RequestParam(value = "pattern", required = false) String pattern,
                                 @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                                 @RequestParam(value = "size", required = false, defaultValue = PageSize.DEFAULT) int pageSize, Model model) {
        model.addAttribute("parents", projectService.getAllProjects());
        model.addAttribute("years", DateUtil.getAvailableYears());
        model.addAttribute("projects", projectService.getPageFiltered(StringUtil.replaceNullWithEmptyString(pattern), pageNumber, pageSize));
        model.addAttribute("projectForm", new ProjectDTO());
        model.addAttribute("callingInterface", "projects");
        return "select-project";
    }

    @GetMapping(value = "/delete-project")
    public String deleteProject(@RequestParam("id") Integer id, Model model) {
        if (projectService.existsProjectById(id)) {
            projectService.deleteProjectById(id);
        }
        return "redirect:/projects";
    }

    @PostMapping(value = "/update-project")
    public String updateProject(
            @ModelAttribute("projectForm") @Valid ProjectDTO projectDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("ERROR: " +
                    bindingResult.getAllErrors().stream().map(ObjectError::getObjectName).collect(Collectors.joining(",")));
        }
        projectService.save(projectDTO);
        return "redirect:/projects";
    }
}