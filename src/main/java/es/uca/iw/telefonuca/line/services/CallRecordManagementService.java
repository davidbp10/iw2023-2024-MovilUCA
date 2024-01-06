package es.uca.iw.telefonuca.line.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.telefonuca.line.repositories.CallRecordRepository;
import es.uca.iw.telefonuca.contract.repositories.ContractRepository;
import es.uca.iw.telefonuca.line.repositories.CustomerLineRepository;

import es.uca.iw.telefonuca.line.domain.CallRecord;
import es.uca.iw.telefonuca.line.domain.CustomerLine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CallRecordManagementService {

    private final CallRecordRepository repository;
    private final CustomerLineRepository customerLineRepository;

    public CallRecordManagementService(CallRecordRepository repository, CustomerLineRepository customerLineRepository,
            ContractRepository contractRepository) {
        this.repository = repository;
        this.customerLineRepository = customerLineRepository;
    }

    @Transactional
    public List<CallRecord> loadAll() {
        return repository.findAll();
    }

    @Transactional
    public List<CallRecord> loadCallRecordsByContractId(UUID contractId) {
        // Busca todas las líneas de cliente con el ContractId dado
        List<CustomerLine> customerLines = customerLineRepository.findByContractId(contractId);
        if (customerLines.isEmpty()) {
            throw new RuntimeException("No customer lines present with contract id: " + contractId);
        }

        // Para cada línea de cliente, obtiene el número de teléfono de la línea de
        // cliente
        // y busca todas las llamadas con ese número de teléfono
        List<CallRecord> callRecords = new ArrayList<>();
        for (CustomerLine customerLine : customerLines) {
            int phoneNumber = customerLine.getPhoneNumber();
            callRecords.addAll(repository.findBySender(phoneNumber));
            callRecords.addAll(repository.findByReceiver(phoneNumber));
        }

        if (callRecords.isEmpty()) {
            throw new RuntimeException("No call records present with contract id: " + contractId);
        } else {
            return callRecords;
        }
    }

    @Transactional
    public List<CallRecord> loadCallRecordsByCustomerLineId(UUID customerLineId) {
        // Busca la línea de cliente con el ID dado
        Optional<CustomerLine> customerLineOpt = customerLineRepository.findById(customerLineId);
        if (customerLineOpt.isEmpty()) {
            throw new RuntimeException("No customer line present with id: " + customerLineId);
        }
        CustomerLine customerLine = customerLineOpt.get();

        // Obtiene el número de teléfono de la línea de cliente
        int phoneNumber = customerLine.getPhoneNumber();

        // Busca todas las llamadas con ese número de teléfono
        List<CallRecord> callRecords = repository.findBySender(phoneNumber);
        callRecords.addAll(repository.findByReceiver(phoneNumber));
        if (callRecords.isEmpty()) {
            throw new RuntimeException("No call records present with phone number: " + phoneNumber);
        } else {
            return callRecords;
        }
    }

    List<CallRecord> loadCallRecordsByDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<CallRecord> callRecord = repository.findByDate(localDate);
        if (callRecord.isEmpty()) {
            throw new RuntimeException("No callRecord present with date: " + date);
        } else {
            return callRecord;
        }
    }

    List<CallRecord> loadCallRecordsByDuration(int duration) {
        List<CallRecord> callRecord = repository.findByDuration(duration);
        if (callRecord.isEmpty()) {
            throw new RuntimeException("No callRecord present with duration: " + duration);
        } else {
            return callRecord;
        }
    }

    @Transactional
    public CallRecord loadCallRecordById(UUID id) {
        Optional<CallRecord> callRecord = repository.findById(id);
        if (callRecord.isEmpty()) {
            throw new RuntimeException("No callRecord present with id: " + id);
        } else {
            return callRecord.get();
        }
    }

    @Transactional
    public List<CallRecord> loadCallRecordsBySender(int phoneNumber) {
        List<CallRecord> callRecord = repository.findBySender(phoneNumber);
        if (callRecord.isEmpty()) {
            throw new RuntimeException("No callRecord present with phoneNumber: " + phoneNumber);
        } else {
            return callRecord;
        }
    }

    @Transactional
    public List<CallRecord> loadCallRecordsByReceiver(int phoneNumber) {
        List<CallRecord> callRecord = repository.findByReceiver(phoneNumber);
        if (callRecord.isEmpty()) {
            throw new RuntimeException("No callRecord present with phoneNumber: " + phoneNumber);
        } else {
            return callRecord;
        }
    }

    @Transactional
    public List<CallRecord> loadCallRecordsByPhoneNumber(int phoneNumber) {
        List<CallRecord> senderCallRecord = repository.findBySender(phoneNumber);
        List<CallRecord> receiverCallRecord = repository.findByReceiver(phoneNumber);
        senderCallRecord.addAll(receiverCallRecord);
        if (senderCallRecord.isEmpty()) {
            throw new RuntimeException("No callRecord present with phoneNumber: " + phoneNumber);
        } else {
            return senderCallRecord;
        }
    }

    public boolean saveCallRecord(CallRecord callRecord) {
        try {
            repository.save(callRecord);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    public void deleteCallRecord(CallRecord callRecord) {
        repository.delete(callRecord);
    }

}
