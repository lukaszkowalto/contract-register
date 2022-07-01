package controlling.contractRegister.export;

import controlling.contractRegister.model.Project;

import java.util.List;

public class ProjectExportObject extends ExportObject<Project> {

    public ProjectExportObject(List<Project> objects) {
        super(objects);
    }

    @Override
    public Class<Project> getObjectClass() {
        return Project.class;
    }
}