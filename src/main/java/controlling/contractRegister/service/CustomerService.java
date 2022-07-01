package controlling.contractRegister.service;

import controlling.contractRegister.model.Customer;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.web.CustomerDTO;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();

    void save(CustomerDTO customerDTO);

    boolean existsCustomerByNip(String nip);

    boolean existsCustomerByPesel(String pesel);

    long getCount();

    Customer getCustomerById(Integer customerId);

    boolean existsCustomerById(Integer customerId);

    void deleteCustomerById(Integer customerId);

    Paged<Customer> getPage(int pageNumber, int size);

    Paged<Customer> getPageFiltered(String pattern, int pageNumber, int size);
}