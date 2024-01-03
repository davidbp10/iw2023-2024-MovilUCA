package es.uca.iw.telefonuca.contract.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.repositories.ContractRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractService {

    private final ContractRepository repository;

    public ContractService(ContractRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<Contract> loadContractByOwnerId(UUID ownerId) throws Exception {
        return repository.findByOwnerId(ownerId);
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
    public List<Contract> loadAllContracts() {
        return repository.findAll();
    }

    public void delete(Contract contract) {
        repository.delete(contract);
    }

    public int count() {
        return (int) repository.count();
    }
}
