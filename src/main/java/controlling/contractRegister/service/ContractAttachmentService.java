package controlling.contractRegister.service;

import controlling.contractRegister.model.ContractAttachment;

import java.util.List;

public interface ContractAttachmentService {

    List<ContractAttachment> getContractAttachmentsByContractId(Integer contractId);

    ContractAttachment getContractAttachmentById(Integer Id);
}