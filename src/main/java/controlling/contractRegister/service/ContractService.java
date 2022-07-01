package controlling.contractRegister.service;

import controlling.contractRegister.model.Contract;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.web.ContractDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ContractService {

    void save(ContractDTO contractDTO);

    List<Contract> getAllContracts();

    List<Contract> getTop5ContractsOrderByIdDesc();

    boolean existsContractById(Integer contractId);

    boolean existsContractByContractNumber(String contractNumber);

    void deleteContractById(Integer contractId);

    Integer getNumberOfContractsByCreateDateTimeBetween(LocalDateTime createDateTimeStart, LocalDateTime createDateTimeEnd);

    double getSumOfContractsGrossAmountByCreateDateTimeBetween(LocalDateTime createDateTimeStart, LocalDateTime createDateTimeEnd);

    Paged<Contract> getPage(int pageNumber, int size);

    Paged<Contract> getPageFiltered(String pattern, int pageNumber, int size);
}