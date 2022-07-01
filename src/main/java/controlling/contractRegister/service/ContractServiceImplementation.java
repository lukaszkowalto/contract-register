package controlling.contractRegister.service;

import controlling.contractRegister.model.Contract;
import controlling.contractRegister.model.ContractAttachment;
import controlling.contractRegister.model.Customer;
import controlling.contractRegister.model.FinancialDistributionMatrix;
import controlling.contractRegister.pagination.Paged;
import controlling.contractRegister.pagination.Paging;
import controlling.contractRegister.repository.*;
import controlling.contractRegister.util.FileUtil;
import controlling.contractRegister.util.StringUtil;
import controlling.contractRegister.web.ContractAttachmentDTO;
import controlling.contractRegister.web.ContractDTO;
import controlling.contractRegister.web.FinancialDistributionMatrixDTO;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ContractServiceImplementation implements ContractService {

    @Autowired
    private ContractAttachmentRepository contractAttachmentRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public void save(ContractDTO contractDTO) {
        Contract contractDB = map(contractDTO);
        contractDB.setContractAttachments(new ArrayList<>());

        List<ContractAttachmentDTO> caDTO = Optional.ofNullable(contractDTO.getContractAttachmentDTOS())
                .orElse(Collections.emptyList())
                .stream()
                .filter(c -> c.getDescription() != null)
                .collect(toList());

        Map<Boolean, List<ContractAttachmentDTO>> attachmentsByIdPresence = caDTO.stream()
                .collect(Collectors.partitioningBy(attachment -> attachment.getId() != null));

        List<ContractAttachmentDTO> newAttachmentsDTO = attachmentsByIdPresence.getOrDefault(Boolean.FALSE, Collections.emptyList());
        List<ContractAttachment> newAttachments = mapAttachments(contractDB, newAttachmentsDTO);

        if (contractDTO.getId() != null) {
            List<ContractAttachmentDTO> attachmentsWithIds = attachmentsByIdPresence.getOrDefault(Boolean.TRUE, Collections.emptyList());
            Map<Integer, String> attachmentIdToDescription = attachmentsWithIds.stream()
                    .collect(Collectors.toMap(ContractAttachmentDTO::getId, ContractAttachmentDTO::getDescription));

            List<ContractAttachment> attachmentsFromDB = contractAttachmentRepository.findAllByContractId(contractDTO.getId());

            Map<Boolean, List<ContractAttachment>> attachmentsByStatus = attachmentsFromDB.stream()
                    .collect(Collectors.partitioningBy(attachment -> attachmentIdToDescription.containsKey(attachment.getId())));

            List<ContractAttachment> attachmentsToUpdate = attachmentsByStatus.getOrDefault(Boolean.TRUE, Collections.emptyList())
                    .stream()
                    .map(attachment -> getAttachmentWithDescription(attachment, attachmentIdToDescription.get(attachment.getId())))
                    .collect(toList());

            deleteFromDisc(attachmentsByStatus.getOrDefault(Boolean.FALSE, Collections.emptyList()));

            List<ContractAttachment> unionOfAttachments = new ArrayList<>();
            unionOfAttachments = ListUtils.union(newAttachments, attachmentsToUpdate);

            contractDB.setContractAttachments(unionOfAttachments);

        } else {
            contractDB.setContractAttachments(newAttachments);
        }

        contractDB.setFinancialDistributionMatrix(mapFinancialMatrix(contractDB, contractDTO.getFdmDTOS()));
        contractRepository.save(contractDB);
    }

    @Override
    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public List<Contract> getTop5ContractsOrderByIdDesc() {
        return contractRepository.findTop5ByOrderByIdDesc();
    }

    @Override
    public boolean existsContractById(Integer contractId) {
        return contractRepository.existsById(contractId);
    }

    @Override
    public boolean existsContractByContractNumber(String contractNumber) {
        return contractRepository.existsByContractNumber(contractNumber);
    }

    @Override
    @Transactional
    public void deleteContractById(Integer contractId) {
        List<ContractAttachment> contractAttachments = contractAttachmentRepository.findAllByContractId(contractId);
        contractAttachments.forEach(this::deleteAttachment);
        contractRepository.deleteById(contractId);
    }

    @Override
    public Integer getNumberOfContractsByCreateDateTimeBetween(LocalDateTime createDateTimeStart, LocalDateTime createDateTimeEnd) {
        return Optional.ofNullable(contractRepository.countByCreateDateTimeBetween(createDateTimeStart, createDateTimeEnd)).orElse(0);
    }

    @Override
    public double getSumOfContractsGrossAmountByCreateDateTimeBetween(LocalDateTime createDateTimeStart, LocalDateTime createDateTimeEnd) {
        return Optional.ofNullable(contractRepository.sumGrossAmountByCreateDateTimeBetween(createDateTimeStart, createDateTimeEnd)).orElse(0.0);
    }

    @Override
    public Paged<Contract> getPage(int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<Contract> contractPage = contractRepository.findAll(request);
        return new Paged<>(contractPage, Paging.of(contractPage.getTotalPages(), pageNumber, size));
    }

    @Override
    public Paged<Contract> getPageFiltered(String pattern, int pageNumber, int size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by("id").ascending());
        Page<Contract> contractPage = contractRepository.getAllContractsFiltered(pattern, request);
        return new Paged<>(contractPage, Paging.of(contractPage.getTotalPages(), pageNumber, size));
    }

    private ContractAttachment getAttachmentWithDescription(ContractAttachment attachment, String description) {
        ContractAttachment attachmentWithNewDescription = new ContractAttachment();
        BeanUtils.copyProperties(attachment, attachmentWithNewDescription);
        attachmentWithNewDescription.setDescription(description);
        return attachmentWithNewDescription;
    }

    private Contract map(ContractDTO contractDTO) {
        Contract contractDB = new Contract();
        BeanUtils.copyProperties(contractDTO, contractDB);
        Customer customer = customerRepository.getById(contractDTO.getCustomerId());
        contractDB.setCustomer(customer);
        contractDB.setRemarks(StringUtil.replaceEmptyStringWithNull(contractDB.getRemarks()));
        return contractDB;
    }

    private List<ContractAttachment> mapAttachments(Contract contractDB, List<ContractAttachmentDTO> attachments) {
        return attachments
                .stream()
                .map(this::map)
                .peek(c -> c.setContract(contractDB))
                .collect(toList());
    }

    private List<FinancialDistributionMatrix> mapFinancialMatrix(Contract contractDB, List<FinancialDistributionMatrixDTO> fdmDTOs) {
        return fdmDTOs.stream()
                .filter(f -> f.getCategoryId() != null)
                .map(this::map)
                .peek(f -> f.setContract(contractDB))
                .collect(toList());
    }

    private FinancialDistributionMatrix map(FinancialDistributionMatrixDTO fdm) {
        FinancialDistributionMatrix matrix = new FinancialDistributionMatrix();
        BeanUtils.copyProperties(fdm, matrix);
        matrix.setCategory(categoryRepository.getById(fdm.getCategoryId()));
        matrix.setProject(projectRepository.getById(fdm.getProjectId()));
        return matrix;
    }

    private ContractAttachment map(ContractAttachmentDTO ca) {
        ContractAttachment contractAttachment = new ContractAttachment();
        try {
            UUID token = UUID.randomUUID();
            Optional.ofNullable(ca.getFile().getOriginalFilename())
                    .map(StringUtils::cleanPath)
                    .ifPresent(contractAttachment::setFileName);
            contractAttachment.setToken(token);
            contractAttachment.setDirectory(fileStorageService.getUploadPath().toString());
            contractAttachment.setDocumentFormat(FileUtil.getFileExtension(ca.getFile()).orElse(null));
            contractAttachment.setDescription(ca.getDescription());
            fileStorageService.save(ca.getFile(), token);
        } catch (Exception e) {
            throw new RuntimeException("ERROR: " + e.getMessage());
        }
        return contractAttachment;
    }

    private void deleteFromDisc(List<ContractAttachment> attachmentsToDelete) {
        attachmentsToDelete.forEach(this::deleteAttachment);
    }

    private void deleteAttachment(ContractAttachment attachment) {
        fileStorageService.delete(new File(attachment.getDirectory() + "\\" + attachment.getToken() + "." + attachment.getDocumentFormat()));
    }
}