package controlling.contractRegister.service;

import controlling.contractRegister.model.Customer;
import controlling.contractRegister.model.CustomerAddress;
import controlling.contractRegister.model.CustomerDetails;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.pagination.Paging;
import controlling.contractRegister.repository.CustomerRepository;
import controlling.contractRegister.util.StringUtil;
import controlling.contractRegister.web.CustomerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImplementation implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public void save(CustomerDTO customerDTO) {
        Customer customerDB = new Customer();
        CustomerAddress customerAddressDB = new CustomerAddress();
        CustomerDetails customerDetailsDB = new CustomerDetails();

        BeanUtils.copyProperties(customerDTO, customerAddressDB);
        BeanUtils.copyProperties(customerDTO, customerDetailsDB);
        BeanUtils.copyProperties(customerDTO, customerDB);

        customerAddressDB.setCustomer(customerDB);
        customerAddressDB.setStreet(StringUtil.replaceEmptyStringWithNull(customerAddressDB.getStreet()));
        customerAddressDB.setFlatNumber(StringUtil.replaceEmptyStringWithNull(customerAddressDB.getFlatNumber()));
        customerAddressDB.setZipCode(StringUtil.replaceEmptyStringWithNull(customerAddressDB.getZipCode()));

        customerDB.setCustomerAddress(customerAddressDB);

        String email = StringUtil.replaceEmptyStringWithNull(customerDetailsDB.getEmail());
        String phone = StringUtil.replaceEmptyStringWithNull(customerDetailsDB.getCellPhone());
        String regon = StringUtil.replaceEmptyStringWithNull(customerDetailsDB.getRegon());
        String krs = StringUtil.replaceEmptyStringWithNull(customerDetailsDB.getKrs());

        if (email != null || phone != null || regon != null || krs != null) {
            customerDetailsDB.setEmail(email);
            customerDetailsDB.setCellPhone(phone);
            customerDetailsDB.setKrs(krs);
            customerDetailsDB.setRegon(regon);
            customerDetailsDB.setCustomer(customerDB);
            customerDB.setCustomerDetails(customerDetailsDB);
        }

        customerRepository.save(customerDB);
    }

    @Override
    public boolean existsCustomerByNip(String nip) {
        return customerRepository.existsByNip(nip);
    }

    @Override
    public boolean existsCustomerByPesel(String pesel) {
        return customerRepository.existsByPesel(pesel);
    }

    @Override
    public long getCount() {
        return customerRepository.count();
    }

    @Override
    public boolean existsCustomerById(Integer customerId) {
        return customerRepository.existsById(customerId);
    }

    @Override
    public Customer getCustomerById(Integer customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("ERROR: Customer not found for id :: " + customerId));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public Paged<Customer> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<Customer> customerPage = customerRepository.findAll(request);
        return new Paged<>(customerPage, Paging.of(customerPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public Paged<Customer> getPageFiltered(String pattern, int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<Customer> customerPage = customerRepository.getAllCustomersFiltered(pattern, request);
        return new Paged<>(customerPage, Paging.of(customerPage.getTotalPages(), pageNumber, size));
    }
}