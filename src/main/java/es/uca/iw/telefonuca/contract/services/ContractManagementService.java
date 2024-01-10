package es.uca.iw.telefonuca.contract.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.repositories.ContractRepository;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.repositories.CustomerLineRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractManagementService {

    private final ContractRepository repository;
    private final CustomerLineRepository customerLineRepository;

    public ContractManagementService(ContractRepository repository, CustomerLineRepository customerLineRepository) {
        this.repository = repository;
        this.customerLineRepository = customerLineRepository;
    }

    @Transactional
    public List<Contract> loadContractsByOwnerId(UUID ownerId) {
        List<Contract> contracts = repository.findByOwnerId(ownerId);
        if (contracts.isEmpty()) {
            throw new RuntimeException("No contract present with ownerId: " + ownerId);
        } else {
            return contracts;
        }
    }

    @Transactional
    public Contract loadContractById(UUID contractId) throws Exception {
        Optional<Contract> contract = repository.findById(contractId);
        if (contract.isEmpty()) {
            throw new Exception("No contract present with id: " + contractId);
        } else {
            return contract.get();
        }
    }

    @Transactional
    public List<Contract> loadAll() {
        return repository.findAll();
    }

    @Transactional
    public Contract saveContract(Contract contract) {
        return repository.save(contract);
    }

    @Transactional
    public Contract saveContractWithCustomerLine(Contract contract, CustomerLine customerLine) {
        // Guardar el contrato sin asignarle un customerLine
        Contract savedContract = repository.save(contract);

        // Obtener el ID del contrato recién creado
        UUID contractId = savedContract.getId();

        // Asignar el ID del contrato al customerLine
        customerLine.setContractId(contractId);

        // Guardar el customerLine
        customerLineRepository.save(customerLine);

        return savedContract;
    }

    public void delete(Contract contract) {
        repository.delete(contract);
    }

    public int count() {
        return (int) repository.count();
    }
}
