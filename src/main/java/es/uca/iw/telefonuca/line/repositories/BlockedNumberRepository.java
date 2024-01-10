package es.uca.iw.telefonuca.line.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.line.domain.BlockedNumber;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlockedNumberRepository extends JpaRepository<BlockedNumber, UUID> {
    Optional<BlockedNumber> findById(UUID id);

    List<BlockedNumber> findByBlocker(int phoneNumber);

    List<BlockedNumber> findByBlocked(int phoneNumber);

    Optional<BlockedNumber> findByBlockerAndBlocked(int blocker, int blocked);
}
