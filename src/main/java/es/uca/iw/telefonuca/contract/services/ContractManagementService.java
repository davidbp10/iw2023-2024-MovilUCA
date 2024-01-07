package es.uca.iw.telefonuca.contract.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.repositories.ContractRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractManagementService {

    private final ContractRepository repository;

    public ContractManagementService(ContractRepository repository) {
        this.repository = repository;
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

    public void delete(Contract contract) {
        repository.delete(contract);
    }

    public int count() {
        return (int) repository.count();
    }
}
