package es.uca.iw.telefonuca.line.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.line.domain.Line;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LineRepository extends JpaRepository<Line, UUID> {
    Optional<Line> findById(UUID id);

    Optional<Line> findByName(String name);

    List<Line> findByDescription(String description);

    List<Line> findByPricePerMinute(float pricePerMinute);

    List<Line> findByPricePerMegabyte(float pricePerMegabyte);

    List<Line> findByMinimumMonths(int minimumMonths);

    List<Line> findByFreeMinutes(int freeMinutes);

    List<Line> findByFreeMegabytes(int freeMegabytes);

    Optional<Line> findById(String id);

}
