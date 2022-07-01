package controlling.contractRegister.repository;

import controlling.contractRegister.model.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer> {

    boolean existsByContractNumber(String contractNumber);

    Integer countByCreateDateTimeBetween(LocalDateTime createDateTimeStart, LocalDateTime createDateTimeEnd);

    List<Contract> findTop5ByOrderByIdDesc();

    @Query("SELECT c FROM Contract c WHERE \n" +
            "\t\t\tLOWER(COALESCE(c.contractNumber, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(COALESCE(c.contractDetails, '')) LIKE LOWER(CONCAT('%', :pattern,'%'))\n" +
            "\t\tOR LOWER(COALESCE(c.remarks, '')) LIKE LOWER(CONCAT('%', :pattern,'%')) \n" +
            "\t\tOR LOWER(COALESCE(c.customer.firstName, '')) LIKE LOWER(CONCAT('%', :pattern,'%'))\n" +
            "\t\tOR LOWER(COALESCE(c.customer.lastName, '')) LIKE LOWER(CONCAT('%', :pattern,'%'))\n" +
            "\t\tOR LOWER(COALESCE(c.customer.pesel, '')) LIKE LOWER(CONCAT('%', :pattern,'%'))\n" +
            "\t\tOR LOWER(COALESCE(c.customer.name, '')) LIKE LOWER(CONCAT('%', :pattern,'%'))\n" +
            "\t\tOR LOWER(COALESCE(c.customer.nip, '')) LIKE LOWER(CONCAT('%', :pattern,'%'))")
    Page<Contract> getAllContractsFiltered(@Param("pattern") String pattern, Pageable pageable);

    @Query("SELECT SUM((c.amount+c.amountVAT) * c.exchangeRate ) FROM Contract c WHERE c.createDateTime BETWEEN :createDateTimeStart AND :createDateTimeEnd")
    Double sumGrossAmountByCreateDateTimeBetween(@Param("createDateTimeStart") LocalDateTime createDateTimeStart, @Param("createDateTimeEnd") LocalDateTime createDateTimeEnd);
}