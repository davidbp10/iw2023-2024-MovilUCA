package es.uca.iw.telefonuca.line.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.line.domain.CallRecord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CallRecordRepository extends JpaRepository<CallRecord, UUID> {
    Optional<CallRecord> findById(UUID id);

    List<CallRecord> findBySender(int phoneNumber);

    List<CallRecord> findByReceiver(int phoneNumber);
}
