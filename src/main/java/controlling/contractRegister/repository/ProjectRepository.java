package controlling.contractRegister.repository;

import controlling.contractRegister.dic.ProjectType;
import controlling.contractRegister.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByYearAndProjectType(double year, ProjectType projectType);

    List<Project> findByParentProject(Project parentProject);

    boolean existsByYearAndSymbol(double year, String symbol);

    @Query("SELECT i FROM Project i WHERE \n" +
            "\t\t\tLOWER(coalesce(i.symbol, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(coalesce(i.name, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(coalesce(i.description, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(cast(i.year as string)) LIKE LOWER(CONCAT('%', :pattern,'%'))")
    Page<Project> getAllProjectsFiltered(@Param("pattern") String pattern, Pageable pageable);

    @Query("SELECT i FROM Project i WHERE i.year=(:year) AND NOT EXISTS (SELECT 1 FROM Project ii WHERE ii.parentProject.id = i.id)")
    List<Project> getFDMProjects(@Param("year") double year);

    @Query(value = "WITH CTE AS\n" +
            "(\n" +
            "  SELECT P.id, P.parent_project_id\n" +
            "  FROM dbo.project P\n" +
            "  WHERE P.parent_project_id = :id\n" +
            "\n" +
            "  UNION ALL\n" +
            "\n" +
            "  SELECT P1.id, P1.parent_project_id\n" +
            "  FROM dbo.project P1  \n" +
            "  JOIN CTE M\n" +
            "  ON M.id = P1.parent_project_id\n" +
            " )\n" +
            "SELECT P.* FROM dbo.project P\n" +
            "JOIN CTE C ON P.id=C.id",
            nativeQuery = true)
    List<Project> getChildProjects(@Param("id") Integer id);
}