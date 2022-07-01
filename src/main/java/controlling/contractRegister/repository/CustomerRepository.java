package controlling.contractRegister.repository;

import controlling.contractRegister.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByNip(String nip);

    boolean existsByPesel(String pesel);

    @Query("SELECT c FROM Customer c WHERE  \n" +
            "\t\t\tLOWER(coalesce(c.firstName, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(coalesce(c.lastName, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(coalesce(c.name, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(coalesce(c.pesel, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(coalesce(c.nip, '')) LIKE LOWER(CONCAT('%', :pattern,'%'))")
    Page<Customer> getAllCustomersFiltered(@Param("pattern") String pattern, Pageable pageable);
}