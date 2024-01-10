package es.uca.iw.telefonuca.line.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.repositories.ContractRepository;
import es.uca.iw.telefonuca.line.domain.BlockedNumber;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.repositories.BlockedNumberRepository;
import es.uca.iw.telefonuca.line.repositories.CustomerLineRepository;

@Service
public class BlockedNumberManagementService {

    private final BlockedNumberRepository blockedNumberRepository;
    private final CustomerLineRepository customerLineRepository;
    private final ContractRepository contractRepository;

    public BlockedNumberManagementService(BlockedNumberRepository blockedNumberRepository,
            CustomerLineRepository customerLineRepository, ContractRepository contractRepository) {
        this.blockedNumberRepository = blockedNumberRepository;
        this.customerLineRepository = customerLineRepository;
        this.contractRepository = contractRepository;
    }

    public List<BlockedNumber> loadAll() {
        return blockedNumberRepository.findAll();
    }

    public List<BlockedNumber> loadBlockedNumberByBlocker(int phoneNumber) {
        return blockedNumberRepository.findByBlocker(phoneNumber);
    }

    public List<BlockedNumber> loadBlockedNumberByBlocked(int phoneNumber) {
        return blockedNumberRepository.findByBlocked(phoneNumber);
    }

    public List<BlockedNumber> loadBlockedNumberByUserId(UUID userId) {
        List<Contract> contracts = contractRepository.findByOwnerId(userId);
        List<BlockedNumber> result = new ArrayList<>();

        for (Contract contract : contracts) {
            List<CustomerLine> customerLines = customerLineRepository.findByContractId(contract.getId());

            for (CustomerLine customerLine : customerLines) {
                int blockerPhoneNumber = customerLine.getPhoneNumber();
                List<BlockedNumber> blockedNumbers = blockedNumberRepository.findByBlocker(blockerPhoneNumber);

                result.addAll(blockedNumbers);
            }
        }

        return result;
    }

    public void saveBlockedNumber(BlockedNumber blockedNumber) {
        blockedNumberRepository.save(blockedNumber);
    }

    public void delete(BlockedNumber blockedNumber) {
        blockedNumberRepository.delete(blockedNumber);
    }

    public void deleteAll(List<BlockedNumber> blockedNumbers) {
        blockedNumberRepository.deleteAll(blockedNumbers);
    }

}
