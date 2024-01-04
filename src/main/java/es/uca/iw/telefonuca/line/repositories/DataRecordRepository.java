package es.uca.iw.telefonuca.line.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.line.domain.DataRecord;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface DataRecordRepository extends JpaRepository<DataRecord, UUID> {
    List<DataRecord> findByPhoneNumber(int phoneNumber);

    List<DataRecord> findByDate(LocalTime date);
}
