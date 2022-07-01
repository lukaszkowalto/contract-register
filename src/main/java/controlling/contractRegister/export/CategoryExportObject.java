package controlling.contractRegister.export;

import controlling.contractRegister.model.Category;

import java.util.List;


public class CategoryExportObject extends ExportObject<Category> {

    public CategoryExportObject(List<Category> objects) {
        super(objects);
    }

    @Override
    public Class<Category> getObjectClass() {
        return Category.class;
    }
}