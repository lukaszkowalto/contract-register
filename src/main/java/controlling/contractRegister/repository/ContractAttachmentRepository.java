package controlling.contractRegister.repository;

import controlling.contractRegister.model.ContractAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractAttachmentRepository extends JpaRepository<ContractAttachment, Integer> {

    List<ContractAttachment> findAllByContractId(Integer contractId);
}