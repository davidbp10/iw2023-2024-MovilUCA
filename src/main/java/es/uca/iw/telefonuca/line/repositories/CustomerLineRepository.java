package es.uca.iw.telefonuca.line.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.line.domain.CustomerLine;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerLineRepository extends JpaRepository<CustomerLine, UUID> {

    Optional<CustomerLine> findById(UUID id);

    List<CustomerLine> findByPricePerMinute(float pricePerMinute);

    List<CustomerLine> findByPricePerMegabyte(float pricePerMegabyte);

    List<CustomerLine> findByFreeMinutes(int freeMinutes);

    List<CustomerLine> findByFreeMegabytes(int freeMegabytes);

    Optional<CustomerLine> findById(String id);
}
