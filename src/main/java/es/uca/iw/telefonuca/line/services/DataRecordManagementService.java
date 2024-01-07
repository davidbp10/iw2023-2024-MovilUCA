package es.uca.iw.telefonuca.line.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import es.uca.iw.telefonuca.line.repositories.DataRecordRepository;
import jakarta.transaction.Transactional;

import es.uca.iw.telefonuca.line.domain.DataRecord;

@Service
public class DataRecordManagementService {
    private final DataRecordRepository dataRecordRepository;

    public DataRecordManagementService(DataRecordRepository dataRecordRepository) {
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

}
