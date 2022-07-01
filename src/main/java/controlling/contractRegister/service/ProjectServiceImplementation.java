package controlling.contractRegister.service;

import controlling.contractRegister.dic.ProjectType;
import controlling.contractRegister.model.Project;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.pagination.Paging;
import controlling.contractRegister.repository.ProjectRepository;
import controlling.contractRegister.util.StringUtil;
import controlling.contractRegister.web.ProjectDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImplementation implements ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public List<Project> getProjectsByYearAndProjectType(double year, ProjectType projectType) {
        return projectRepository.findByYearAndProjectType(year, projectType);
    }

    @Override
    public void save(ProjectDTO projectDTO) {
        if (projectDTO.getDateTo() != null && projectDTO.getDateFrom().after(projectDTO.getDateTo())) {
            throw new IllegalArgumentException("ERROR: Wrong project date range.");
        }

        Project projectDB = new Project();
        BeanUtils.copyProperties(projectDTO, projectDB);
        if (projectDTO.getParentProjectId() != null && existsProjectById(projectDTO.getParentProjectId())) {
            projectDB.setParentProject(getProjectById(projectDTO.getParentProjectId()));
        }
        projectDB.setDescription(StringUtil.replaceEmptyStringWithNull(projectDTO.getDescription()));

        projectRepository.save(projectDB);
    }

    @Override
    public boolean existsProjectByYearAndSymbol(double year, String symbol) {
        return projectRepository.existsByYearAndSymbol(year, symbol);
    }

    @Override
    public boolean existsProjectById(Integer projectId) {
        return projectRepository.existsById(projectId);
    }

    @Override
    public Project getProjectById(Integer projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("ERROR: Category not found for id :: " + projectId));
    }

    @Override
    public void deleteProjectById(Integer projectId) {
        onDeleteSetNull(projectId);
        projectRepository.deleteById(projectId);
    }

    @Override
    public List<Project> getFDMProjects(double year) {
        return projectRepository.getFDMProjects(year);
    }

    @Override
    public Paged<Project> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<Project> projectPage = projectRepository.findAll(request);
        return new Paged<>(projectPage, Paging.of(projectPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public Paged<Project> getPageFiltered(String pattern, int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<Project> projectPage = projectRepository.getAllProjectsFiltered(pattern, request);
        return new Paged<>(projectPage, Paging.of(projectPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public List<Project> getAllChildProjects(Integer projectId) {
        return projectRepository.getChildProjects(projectId);
    }

    private void onDeleteSetNull(Integer parentProjectId) {
        for (Project childProject : projectRepository.findByParentProject(projectRepository.getById(parentProjectId))) {
            childProject.setParentProject(null);
            projectRepository.save(childProject);
        }
    }
}