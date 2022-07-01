package controlling.contractRegister.export;

import controlling.contractRegister.model.Contract;

import java.util.List;

public class ContractExportObject extends ExportObject<Contract> {

    public ContractExportObject(List<Contract> objects) {
        super(objects);
    }

    @Override
    public Class<Contract> getObjectClass() {
        return Contract.class;
    }
}