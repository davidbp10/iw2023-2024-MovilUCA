package es.uca.iw.telefonuca.contract.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.contract.domain.Contract;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContractRepository extends JpaRepository<Contract, UUID> {

    Optional<Contract> findById(UUID id);

    List<Contract> findByOwnerId(UUID ownerId);

    List<Contract> findByMonthsAgreedLessThan(int monthsAgreed);

    List<Contract> findByMonthsAgreedGreaterThan(int monthsAgreed);

    List<Contract> findByMonthsAgreedEquals(int monthsAgreed);

    List<Contract> findByMonthsAgreedBetween(int monthsAgreed1, int monthsAgreed2);

    List<Contract> findByMonthsAgreedLessThanEqual(int monthsAgreed);

    List<Contract> findByMonthsAgreedGreaterThanEqual(int monthsAgreed);

    List<Contract> findByStartAtBefore(LocalDate date);

    List<Contract> findByStartAtAfter(LocalDate date);

}