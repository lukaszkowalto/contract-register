package controlling.contractRegister.export;

import controlling.contractRegister.model.Customer;

import java.util.List;

public class CustomerExportObject extends ExportObject<Customer> {

    public CustomerExportObject(List<Customer> objects) {
        super(objects);
    }

    @Override
    public Class<Customer> getObjectClass() {
        return Customer.class;
    }
}