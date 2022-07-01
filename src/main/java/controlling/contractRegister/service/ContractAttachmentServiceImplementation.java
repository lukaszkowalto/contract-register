package controlling.contractRegister.service;

import controlling.contractRegister.model.ContractAttachment;
import controlling.contractRegister.repository.ContractAttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractAttachmentServiceImplementation implements ContractAttachmentService {

    @Autowired
    private ContractAttachmentRepository contractAttachmentRepository;

    @Override
    public List<ContractAttachment> getContractAttachmentsByContractId(Integer contractId) {
        return contractAttachmentRepository.findAllByContractId(contractId);
    }

    @Override
    public ContractAttachment getContractAttachmentById(Integer Id) {
        return contractAttachmentRepository.findById(Id)
                .orElseThrow(() -> new RuntimeException(" ContractAttachment not found for id :: " + Id));
    }
}