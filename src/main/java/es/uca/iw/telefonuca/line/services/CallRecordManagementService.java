package es.uca.iw.telefonuca.line.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uca.iw.telefonuca.line.repositories.CallRecordRepository;

import es.uca.iw.telefonuca.line.domain.CallRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CallRecordManagementService {

    private final CallRecordRepository repository;

    public CallRecordManagementService(CallRecordRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<CallRecord> loadAll() {
        return repository.findAll();
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
    public List<CallRecord> loadCallRecordBySender(int phoneNumber) {
        List<CallRecord> callRecord = repository.findBySender(phoneNumber);
        if (callRecord.isEmpty()) {
            throw new RuntimeException("No callRecord present with phoneNumber: " + phoneNumber);
        } else {
            return callRecord;
        }
    }

    @Transactional
    public List<CallRecord> loadCallRecordByReceiver(int phoneNumber) {
        List<CallRecord> callRecord = repository.findByReceiver(phoneNumber);
        if (callRecord.isEmpty()) {
            throw new RuntimeException("No callRecord present with phoneNumber: " + phoneNumber);
        } else {
            return callRecord;
        }
    }

    @Transactional
    public List<CallRecord> loadCallRecordByPhoneNumber(int phoneNumber) {
        List<CallRecord> senderCallRecord = repository.findBySender(phoneNumber);
        List<CallRecord> receiverCallRecord = repository.findByReceiver(phoneNumber);
        senderCallRecord.addAll(receiverCallRecord);
        if (senderCallRecord.isEmpty()) {
            throw new RuntimeException("No callRecord present with phoneNumber: " + phoneNumber);
        } else {
            return senderCallRecord;
        }
    }

    @Transactional
    public void saveCallRecord(CallRecord callRecord) {
        repository.save(callRecord);
    }

    @Transactional
    public void deleteCallRecord(CallRecord callRecord) {
        repository.delete(callRecord);
    }

}
