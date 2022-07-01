package controlling.contractRegister.service;

import controlling.contractRegister.dic.ProjectType;
import controlling.contractRegister.model.Project;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.web.ProjectDTO;

import java.util.List;

public interface ProjectService {

    List<Project> getProjectsByYearAndProjectType(double year, ProjectType projectType);

    void save(ProjectDTO projectDTO);

    boolean existsProjectByYearAndSymbol(double year, String symbol);

    boolean existsProjectById(Integer projectId);

    Project getProjectById(Integer projectId);

    void deleteProjectById(Integer projectId);

    List<Project> getFDMProjects(double year);

    Paged<Project> getPage(int pageNumber, int size);

    Paged<Project> getPageFiltered(String pattern, int pageNumber, int size);

    List<Project> getAllProjects();

    List<Project> getAllChildProjects(Integer projectId);
}