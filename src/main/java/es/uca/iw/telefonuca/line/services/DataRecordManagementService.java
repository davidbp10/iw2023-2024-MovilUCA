package es.uca.iw.telefonuca.line.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.repositories.ContractRepository;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.domain.DataRecord;
import es.uca.iw.telefonuca.line.repositories.CustomerLineRepository;
import es.uca.iw.telefonuca.line.repositories.DataRecordRepository;
import jakarta.transaction.Transactional;

@Service
public class DataRecordManagementService {
    private final DataRecordRepository dataRecordRepository;
    private final ContractRepository contractRepository;
    private final CustomerLineRepository customerLineRepository;

    public DataRecordManagementService(DataRecordRepository dataRecordRepository,
            ContractRepository contractRepository, CustomerLineRepository customerLineRepository) {
        this.contractRepository = contractRepository;
        this.customerLineRepository = customerLineRepository;
        this.dataRecordRepository = dataRecordRepository;
    }

    @Transactional
    public List<DataRecord> loadAll() {
        return dataRecordRepository.findAll();
    }

    @Transactional
    public DataRecord loadDataRecordById(UUID id) {
        Optional<DataRecord> dataRecord = dataRecordRepository.findById(id);
        if (dataRecord.isEmpty()) {
            throw new RuntimeException("No dataRecord present with id: " + id);
        } else {
            return dataRecord.get();
        }
    }

    @Transactional
    public List<DataRecord> loadDataRecordByDate(LocalDate date) {
        List<DataRecord> dataRecord = dataRecordRepository.findByDate(date);
        if (dataRecord.isEmpty()) {
            throw new RuntimeException("No dataRecord present with date: " + date);
        } else {
            return dataRecord;
        }
    }

    @Transactional
    public List<DataRecord> loadDataRecordByPhoneNumber(int phoneNumber) {
        List<DataRecord> dataRecord = dataRecordRepository.findByPhoneNumber(phoneNumber);
        if (dataRecord.isEmpty()) {
            throw new RuntimeException("No dataRecord present with phoneNumber: " + phoneNumber);
        } else {
            return dataRecord;
        }
    }

    @Transactional
    public DataRecord saveDataRecord(DataRecord dataRecord) {
        return dataRecordRepository.save(dataRecord);
    }

    @Transactional
    public List<DataRecord> loadDataRecordByUserId(UUID userId) {
        // Find all the contracts belonging to the user
        List<Contract> contracts = contractRepository.findByOwnerId(userId);
        List<UUID> contractIds = contracts.stream()
                .map(Contract::getId)
                .collect(Collectors.toList());

        // Find all the customer lines associated with those contracts
        List<CustomerLine> customerLines = customerLineRepository.findByContractIdIn(contractIds);
        List<Integer> phoneNumbers = customerLines.stream()
                .map(CustomerLine::getPhoneNumber)
                .collect(Collectors.toList());

        // Find all the data records for those phone numbers
        List<DataRecord> dataRecords = new ArrayList<>();
        for (Integer phoneNumber : phoneNumbers) {
            dataRecords.addAll(dataRecordRepository.findByPhoneNumber(phoneNumber));
        }

        return dataRecords;
    }

    @Transactional
    public List<DataRecord> loadDataRecordByUserIdAndDates(UUID userId, LocalDate startDate, LocalDate endDate) {
        // Reuse loadDataRecordByUserId to get all data records for the user
        List<DataRecord> allDataRecordsForUser = loadDataRecordByUserId(userId);

        // Filter the data records to only include those within the given date range
        return allDataRecordsForUser.stream()
                .filter(dataRecord -> !dataRecord.getDate().isBefore(startDate)
                        && !dataRecord.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

}
