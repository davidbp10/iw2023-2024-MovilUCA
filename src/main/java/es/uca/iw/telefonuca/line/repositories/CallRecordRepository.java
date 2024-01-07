package es.uca.iw.telefonuca.line.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.line.domain.CallRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CallRecordRepository extends JpaRepository<CallRecord, UUID> {
    Optional<CallRecord> findById(UUID id);

    List<CallRecord> findBySender(int phoneNumber);

    List<CallRecord> findByReceiver(int phoneNumber);

    List<CallRecord> findByDate(LocalDate date);

    List<CallRecord> findByDuration(int duration);

    List<CallRecord> findBySenderAndDate(int phoneNumber, LocalDate date);

    List<CallRecord> findBySenderAndDateBetween(int phoneNumber, LocalDate date, LocalDate date2);

    List<CallRecord> findByReceiverAndDate(int phoneNumber, LocalDate date);

    List<CallRecord> findByReceiverAndDateBetween(int phoneNumber, LocalDate date, LocalDate date2);

}
